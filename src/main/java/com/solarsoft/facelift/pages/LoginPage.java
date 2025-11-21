package com.solarsoft.facelift.pages;

import com.solarsoft.facelift.config.ConfigReader;
import com.solarsoft.facelift.driver.DriverManager;
import com.solarsoft.facelift.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Login page object mapped to the provided Facelift login HTML.
 */
public class LoginPage {

    private WebDriver driver;

    // Inputs: using name attributes from the HTML you provided
    @FindBy(name = "username")
    private WebElement usernameInput;

    @FindBy(name = "password")
    private WebElement passwordInput;

    // Sign In button: matches button text and class used in HTML
    @FindBy(xpath = "//button[@type='submit' and (contains(.,'Sign In') or contains(.,'Sign in') or contains(.,'Login'))]")
    private WebElement signInButton;

    // Remember me checkbox (id="html" in provided HTML)
    @FindBy(id = "html")
    private WebElement rememberMeCheckbox;

    // Forgot password link
    @FindBy(css = "a.forgot-button")
    private WebElement forgotPasswordLink;

    public LoginPage() {
        this.driver = DriverManager.getDriver();
        PageFactory.initElements(driver, this);
    }

    /** Clear and type into username field */
    public void setUsername(String username) {
        WebElement el = WaitUtils.waitForVisibility(usernameInput, 8);
        el.clear();
        el.sendKeys(username);
    }

    /** Clear and type into password field */
    public void setPassword(String password) {
        WebElement el = WaitUtils.waitForVisibility(passwordInput, 8);
        el.clear();
        el.sendKeys(password);
    }

    /** Click the Sign In button */
    public void clickSignIn() {
        WaitUtils.clickWhenClickable(signInButton, 8);
    }

    /** Convenience: login using provided credentials */
    public void login(String username, String password) {
        setUsername(username);
        setPassword(password);
        clickSignIn();
    }

    /** Convenience: login using credentials from config.properties */
    public void loginWithValidUser() {
        String user = ConfigReader.get("username");
        String pass = ConfigReader.get("password");
        login(user, pass);
    }

    /** Returns true if login page appears (username field visible) */
    public boolean isLoginPageDisplayed() {
        try {
            return WaitUtils.waitForVisibility(usernameInput, 8).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Toggle Remember Me checkbox (checks it if unchecked) */
    public void enableRememberMe() {
        try {
            WebElement cb = WaitUtils.waitForVisibility(rememberMeCheckbox, 5);
            if (!cb.isSelected()) cb.click();
        } catch (Exception ignored) {}
    }

    /** Returns whether Remember Me checkbox is selected */
    public boolean isRememberMeSelected() {
        try {
            WebElement cb = WaitUtils.waitForVisibility(rememberMeCheckbox, 5);
            return cb.isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    /** Click the Forgot password link */
    public void clickForgotPassword() {
        try {
            WaitUtils.clickWhenClickable(forgotPasswordLink, 5);
        } catch (Exception ignored) {}
    }

    /** Read whatever value is currently present in username input (useful for pre-filled value checks) */
    public String getUsernameValue() {
        try {
            return WaitUtils.waitForVisibility(usernameInput, 5).getAttribute("value").trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Try to detect common error messages on the page and return the first seen.
     * Returns empty string if none found.
     */
    public String getErrorMessage() {
        try {
            List<By> locators = Arrays.asList(
                    // common CSS classes
                    By.cssSelector(".error"),
                    By.cssSelector(".error-message"),
                    By.cssSelector(".alert-danger"),
                    By.cssSelector(".toast-error"),
                    // generic alert / role=alert
                    By.xpath("//*[(@role='alert' or contains(@class,'alert')) and string-length(normalize-space(.))>0]"),
                    // small/paragraph near inputs that often show validation text
                    By.xpath("//p[contains(@class,'error') or contains(@class,'validation') or contains(.,'Invalid') or contains(.,'incorrect')][string-length(normalize-space(.))>0]")
            );

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            for (By locator : locators) {
                try {
                    WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    if (el != null) {
                        String txt = el.getText();
                        if (txt != null && txt.trim().length() > 0) return txt.trim();
                    }
                } catch (Exception ignored) {
                    // not found / not visible -> try next
                }
            }
        } catch (Exception ignored) {}
        return "";
    }
}
