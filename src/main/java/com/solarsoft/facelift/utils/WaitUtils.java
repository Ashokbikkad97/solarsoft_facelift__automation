package com.solarsoft.facelift.utils;

import com.solarsoft.facelift.driver.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private static WebDriverWait getWait(int seconds) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(seconds));
    }

    /**
     * Wait until element is visible.
     */
    public static WebElement waitForVisibility(WebElement element, int seconds) {
        return getWait(seconds).until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for element to be clickable.
     */
    public static WebElement waitForClickability(WebElement element, int seconds) {
        return getWait(seconds).until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Click element when clickable.
     */
    public static void clickWhenClickable(WebElement element, int seconds) {
        getWait(seconds).until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /**
     * Wait for presence of element located by By locator.
     */
    public static WebElement waitForPresence(By locator, int seconds) {
        return getWait(seconds).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait until element becomes invisible.
     */
    public static boolean waitForInvisibility(By locator, int seconds) {
        return getWait(seconds).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Send keys after waiting for visibility and readiness.
     */
    public static void sendKeys(WebElement element, String text, int seconds) {
        WebElement el = waitForVisibility(element, seconds);
        el.clear();
        el.sendKeys(text);
    }

    /**
     * Safe click: tries normal click, fallback JS click if needed.
     */
    public static void safeClick(WebElement element, int seconds) {
        try {
            clickWhenClickable(element, seconds);
        } catch (Exception e) {
            // fallback to JS click
            JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
            js.executeScript("arguments[0].click();", element);
        }
    }
}
