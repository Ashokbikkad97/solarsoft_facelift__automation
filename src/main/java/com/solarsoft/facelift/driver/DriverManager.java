package com.solarsoft.facelift.driver;

import com.solarsoft.facelift.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverManager {

    // Thread-safe driver for parallel execution (future ready)
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Initialize Browser
    public static void initDriver() {

        if (driver.get() == null) {

            String browser = ConfigReader.get("browser");

            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driver.set(new ChromeDriver());
                    break;

                // Add these in future when needed:
                /*
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver.set(new FirefoxDriver());
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    driver.set(new EdgeDriver());
                    break;
                */

                default:
                    throw new RuntimeException("Browser not supported: " + browser);
            }

            // Common browser settings
            driver.get().manage().window().maximize();
        }
    }

    // Get driver instance
    public static WebDriver getDriver() {
        return driver.get();
    }

    // Quit driver
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
