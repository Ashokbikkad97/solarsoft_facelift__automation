package com.solarsoft.facelift.pages;

import com.solarsoft.facelift.driver.DriverManager;
import com.solarsoft.facelift.config.ConfigReader;
import com.solarsoft.facelift.utils.WaitUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.io.FileWriter;
import java.time.Instant;
import java.util.List;

/**
 * Final robust LoginPage with:
 * - isLoginPageDisplayed(), getErrorMessage(), enableRememberMe()
 * - loginWithValidUser() with retries and failure diagnostics (saves page source + screenshot)
 * - Uses WaitUtils WebElement overloads (existing in your framework)
 */
public class LoginPage {

    private WebDriver driver;
    private static final int ATTEMPTS = 3;

    @FindBy(name = "username")
    private WebElement usernameInput;

    @FindBy(name = "email")
    private WebElement emailInput; // fallback

    @FindBy(name = "password")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@type='submit' and (contains(.,'Sign In') or contains(.,'Sign in') or contains(.,'Login') or contains(.,'Log in'))]")
    private WebElement submitBtn;

    public LoginPage() {
        this.driver = DriverManager.getDriver();
        PageFactory.initElements(driver, this);
    }

    /**
     * Returns true if login page visible (keeps compatibility for older tests).
     */
    public boolean isLoginPageDisplayed() {
        try {
            if (usernameInput != null) {
                try {
                    WaitUtils.waitForVisibility(usernameInput, 5);
                    if (usernameInput.isDisplayed()) return true;
                } catch (Exception ignored) {}
            }
            if (emailInput != null) {
                try {
                    WaitUtils.waitForVisibility(emailInput, 5);
                    if (emailInput.isDisplayed()) return true;
                } catch (Exception ignored) {}
            }
            if (passwordInput != null) {
                try {
                    WaitUtils.waitForVisibility(passwordInput, 5);
                    if (passwordInput.isDisplayed()) return true;
                } catch (Exception ignored) {}
            }
            if (submitBtn != null) {
                try {
                    WaitUtils.waitForVisibility(submitBtn, 5);
                    if (submitBtn.isDisplayed()) return true;
                } catch (Exception ignored) {}
            }
            List<WebElement> foundUser = driver.findElements(By.cssSelector("input[type='email'], input[placeholder*='Email'], input[name='username'], input[id*='user']"));
            if (!foundUser.isEmpty()) {
                try {
                    WaitUtils.waitForVisibility(foundUser.get(0), 4);
                    if (foundUser.get(0).isDisplayed()) return true;
                } catch (Exception ignored) {}
            }
            List<WebElement> foundPass = driver.findElements(By.cssSelector("input[type='password']"));
            if (!foundPass.isEmpty()) {
                try {
                    WaitUtils.waitForVisibility(foundPass.get(0), 4);
                    if (foundPass.get(0).isDisplayed()) return true;
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Enable the 'Remember me' checkbox if present. Returns whether it is selected after the action.
     */
    public boolean enableRememberMe() {
        try {
            By[] rememberLocators = new By[]{
                    By.cssSelector("input[type='checkbox'][name*='remember']"),
                    By.cssSelector("input[type='checkbox'][id*='remember']"),
                    By.xpath("//label[contains(.,'Remember')]/preceding-sibling::input[@type='checkbox']"),
                    By.xpath("//input[@type='checkbox' and contains(@id,'remember')]")
            };
            for (By loc : rememberLocators) {
                List<WebElement> found = driver.findElements(loc);
                if (!found.isEmpty()) {
                    WebElement chk = found.get(0);
                    if (!chk.isSelected()) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", chk);
                    }
                    return chk.isSelected();
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Get common error message shown on login failure.
     */
    public String getErrorMessage() {
        try {
            By[] errorLocators = new By[]{
                    By.cssSelector(".error-message"),
                    By.cssSelector("p.text-danger"),
                    By.cssSelector(".alert-danger"),
                    By.xpath("//*[contains(text(),'invalid') or contains(text(),'Incorrect') or contains(text(),'wrong') or contains(text(),'failed')]")
            };
            for (By loc : errorLocators) {
                List<WebElement> found = driver.findElements(loc);
                if (!found.isEmpty()) {
                    try { return found.get(0).getText().trim(); } catch (Exception ignored) {}
                }
            }
        } catch (Exception ignored) {}
        return "";
    }

    /**
     * Attempts login using credentials from config. On persistent failure saves diagnostics.
     */
    public void loginWithValidUser() {
        String username = ConfigReader.get("username");
        String password = ConfigReader.get("password");

        for (int i = 1; i <= ATTEMPTS; i++) {
            try {
                login(username, password);

                // wait for dashboard global search (element id 'searchText') to appear
                List<WebElement> searchEls = driver.findElements(By.id("searchText"));
                if (!searchEls.isEmpty()) {
                    WaitUtils.waitForVisibility(searchEls.get(0), 10);
                    System.err.println("Login successful on attempt " + i);
                    return;
                }

                // small pause then retry
                Thread.sleep(800);
            } catch (StaleElementReferenceException | TimeoutException | NoSuchElementException ex) {
                System.err.println("Login attempt " + i + " transient error: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
                safeSleep(700);
            } catch (InterruptedException ignored) {}
        }

        // If we reach here, login failed: collect diagnostics then throw
        long t = Instant.now().getEpochSecond();
        String pageSourcePath = "/mnt/data/login-failure-" + t + ".html";
        String screenshotPath = "/mnt/data/login-failure-" + t + ".png";
        try {
            savePageSource(pageSourcePath);
            saveScreenshot(screenshotPath);
            System.err.println("Login failed after " + ATTEMPTS + " attempts. Page source saved to: " + pageSourcePath);
            System.err.println("Screenshot saved to: " + screenshotPath);
            System.err.println("Detected login error message: '" + getErrorMessage() + "'");
        } catch (Exception e) {
            System.err.println("Failed saving diagnostics: " + e.getMessage());
        }

        throw new RuntimeException("loginWithValidUser() - failed to login after " + ATTEMPTS + " attempts. See diagnostics at: " + pageSourcePath + " and " + screenshotPath);
    }

    /**
     * Core login steps with robust element handling.
     */
    public void login(String userOrEmail, String pwd) {
        dismissPotentialOverlay();

        boolean typedU = safeTypeUsername(userOrEmail);
        if (!typedU) {
            System.err.println("safeTypeUsername() failed - attempting to continue (maybe field name changed).");
            throw new NoSuchElementException("Unable to find username/email input on login page");
        }

        boolean typedP = safeTypePassword(pwd);
        if (!typedP) {
            System.err.println("safeTypePassword() failed - attempting to continue (maybe field name changed).");
            throw new NoSuchElementException("Unable to find password input on login page");
        }

        boolean clicked = safeClickSubmit();
        if (!clicked) {
            WebElement fallback = findAnySubmitLikeButton();
            if (fallback != null) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click()", fallback);
                safeSleep(400);
            } else {
                throw new RuntimeException("Login submit failed: no clickable login button found");
            }
        }
    }

    // ----------------- helper methods -----------------

    private boolean safeTypeUsername(String value) {
        try {
            if (usernameInput != null && usernameInput.isDisplayed()) {
                WaitUtils.waitForVisibility(usernameInput, 8).clear();
                WaitUtils.waitForVisibility(usernameInput, 8).sendKeys(value);
                return true;
            }
        } catch (Exception ignored) {}

        try {
            if (emailInput != null && emailInput.isDisplayed()) {
                WaitUtils.waitForVisibility(emailInput, 8).clear();
                WaitUtils.waitForVisibility(emailInput, 8).sendKeys(value);
                return true;
            }
        } catch (Exception ignored) {}

        By[] possible = new By[]{
                By.cssSelector("input[placeholder*='Email']"),
                By.cssSelector("input[placeholder*='email']"),
                By.cssSelector("input[id*='user']"),
                By.cssSelector("input[id*='email']"),
                By.name("username"),
                By.name("email")
        };
        for (By b : possible) {
            try {
                List<WebElement> found = driver.findElements(b);
                if (!found.isEmpty()) {
                    WebElement el = found.get(0);
                    WaitUtils.waitForVisibility(el, 4).clear();
                    WaitUtils.waitForVisibility(el, 4).sendKeys(value);
                    return true;
                }
            } catch (Exception ignored) {}
        }
        return false;
    }

    private boolean safeTypePassword(String pwd) {
        try {
            if (passwordInput != null && passwordInput.isDisplayed()) {
                WaitUtils.waitForVisibility(passwordInput, 8).clear();
                WaitUtils.waitForVisibility(passwordInput, 8).sendKeys(pwd);
                return true;
            }
        } catch (Exception ignored) {}

        By[] possible = new By[]{
                By.cssSelector("input[type='password']"),
                By.cssSelector("input[name='password']"),
                By.cssSelector("input[id*='pass']")
        };
        for (By b : possible) {
            try {
                List<WebElement> found = driver.findElements(b);
                if (!found.isEmpty()) {
                    WebElement el = found.get(0);
                    WaitUtils.waitForVisibility(el, 4).clear();
                    WaitUtils.waitForVisibility(el, 4).sendKeys(pwd);
                    return true;
                }
            } catch (Exception ignored) {}
        }
        return false;
    }

    private boolean safeClickSubmit() {
        try {
            if (submitBtn != null && submitBtn.isDisplayed()) {
                WaitUtils.clickWhenClickable(submitBtn, 8);
                return true;
            }
        } catch (Exception ignored) {}

        By[] possible = new By[]{
                By.xpath("//button[@type='submit' and (contains(normalize-space(.),'Sign In') or contains(normalize-space(.),'Sign in') or contains(normalize-space(.),'Login') or contains(normalize-space(.),'Log in'))]"),
                By.xpath("//button[contains(.,'Sign In') or contains(.,'Login') or contains(.,'Log in') or contains(.,'Submit')]"),
                By.cssSelector("button.btn-primary[type='submit']"),
                By.cssSelector("button[type='submit']")
        };

        for (By b : possible) {
            try {
                List<WebElement> found = driver.findElements(b);
                if (!found.isEmpty()) {
                    WebElement el = found.get(0);
                    WaitUtils.clickWhenClickable(el, 6);
                    return true;
                }
            } catch (StaleElementReferenceException | TimeoutException ignored) {}
            catch (Exception ignored) {}
        }
        return false;
    }

    private WebElement findAnySubmitLikeButton() {
        try {
            List<WebElement> buttons = driver.findElements(By.tagName("button"));
            for (WebElement b : buttons) {
                String t = b.getText() == null ? "" : b.getText().trim().toLowerCase();
                if (t.contains("sign in") || t.contains("login") || t.contains("log in") || t.contains("submit")) {
                    return b;
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    private void dismissPotentialOverlay() {
        try {
            List<WebElement> cookies = driver.findElements(By.cssSelector("button.cookie-accept, button#onetrust-accept-btn-handler"));
            if (!cookies.isEmpty() && cookies.get(0).isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click()", cookies.get(0));
                safeSleep(300);
            }
        } catch (Exception ignored) {}

        try {
            List<WebElement> closes = driver.findElements(By.cssSelector("button.close, button[aria-label='Close']"));
            if (!closes.isEmpty() && closes.get(0).isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click()", closes.get(0));
                safeSleep(300);
            }
        } catch (Exception ignored) {}
    }

    private void savePageSource(String path) {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(driver.getPageSource());
            fw.flush();
        } catch (Exception e) {
            System.err.println("Failed to save page source: " + e.getMessage());
        }
    }

    private void saveScreenshot(String path) {
        try {
            if (driver instanceof TakesScreenshot) {
                File tmp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                File target = new File(path);
                FileHandler.copy(tmp, target);
            }
        } catch (Exception e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
    }

    private void safeSleep(long millis) {
        try { Thread.sleep(millis); } catch (InterruptedException ignored) {}
    }
}
