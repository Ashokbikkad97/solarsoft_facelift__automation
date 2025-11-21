package com.solarsoft.facelift.tests;

import com.solarsoft.facelift.config.ConfigReader;
import com.solarsoft.facelift.driver.DriverManager;
import com.solarsoft.facelift.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.initDriver();
        driver = DriverManager.getDriver();

        // implicit wait
        try {
            int implicit = Integer.parseInt(ConfigReader.get("implicit.wait"));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicit));
        } catch (Exception e) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        }

        // explicit wait
        try {
            int explicit = Integer.parseInt(ConfigReader.get("explicit.wait"));
            wait = new WebDriverWait(driver, Duration.ofSeconds(explicit));
        } catch (Exception e) {
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        }

        // Open base URL (should be login page or landing page)
        String baseUrl = ConfigReader.get("base.url");
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            throw new RuntimeException("base.url not configured");
        }
        driver.get(baseUrl);

        // Auto-login if enabled and if we are on login page
        try {
            String autoLogin = ConfigReader.get("auto.login");
            if (autoLogin != null && autoLogin.equalsIgnoreCase("true")) {
                LoginPage loginPage = new LoginPage();
                if (loginPage.isLoginPageDisplayed()) {
                    // Use credentials from config.properties
                    loginPage.loginWithValidUser();
                    // optionally wait for dashboard load here (but test will validate)
                } else {
                    // If base.url already lands on dashboard (session persisted), it's fine
                }
            }
        } catch (Exception ignored) {
            // failing auto-login should not break setUp; tests will fail with clear message
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            DriverManager.quitDriver();
        } catch (Exception ignored) {}
    }
}
