package com.solarsoft.facelift.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class AccountPage {
private WebDriver driver;
private By addSiteBtn = By.cssSelector("button[data-testid='add-site'], button[title='Add Site'], //button[normalize-space(.)='Add Site']");


public AccountPage(WebDriver driver) {
this.driver = driver;
}


public void clickAddSite() {
driver.findElement(addSiteBtn).click();
}
}