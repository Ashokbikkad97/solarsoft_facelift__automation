package com.solarsoft.facelift.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class SitePage {
private WebDriver driver;
private By siteNameInput = By.cssSelector("input[name='siteName'], input[id='siteName']");
private By addressInput = By.cssSelector("input[name='address'], textarea[name='address']");
private By pincodeInput = By.cssSelector("input[name='pincode'], input[name='zip']");
private By usageInput = By.cssSelector("input[name*='usage'], input[name='annualUsage']");
private By saveSiteBtn = By.cssSelector("button[type='submit'], button[data-testid='save-site']");


public SitePage(WebDriver driver) {
this.driver = driver;
}


public void fillSite(String name, String address, String pincode, String usage) {
driver.findElement(siteNameInput).clear();
driver.findElement(siteNameInput).sendKeys(name);
driver.findElement(addressInput).clear();
driver.findElement(addressInput).sendKeys(address);
driver.findElement(pincodeInput).clear();
driver.findElement(pincodeInput).sendKeys(pincode);
driver.findElement(usageInput).clear();
driver.findElement(usageInput).sendKeys(usage);
driver.findElement(saveSiteBtn).click();
}
}