package com.solarsoft.facelift.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;


public class AccountsPage {
private WebDriver driver;


// Locators - update if needed to match actual app
private By addAccountBtn = By.cssSelector("button[data-testid='add-account'], button[title='Add Account']");
private By accountNameInput = By.cssSelector("input[name='accountName'], input[id='accountName']");
private By saveBtn = By.cssSelector("button[type='submit'], button[data-testid='save-account']");
private By accountsTableRows = By.cssSelector("table tbody tr");


public AccountsPage(WebDriver driver) {
this.driver = driver;
}


public void clickAddAccount() {
driver.findElement(addAccountBtn).click();
}


public void createAccount(String accountName) {
WebElement name = driver.findElement(accountNameInput);
name.clear();
name.sendKeys(accountName);
driver.findElement(saveBtn).click();
}


public boolean isAccountPresentInList(String accountName) {
String xpath = String.format("//tr[.//td[normalize-space(text())='%s']]", accountName);
return !driver.findElements(By.xpath(xpath)).isEmpty();
}


public void openAccountFromList(String accountName) {
String xpath = String.format("//tr[.//td[normalize-space(text())='%s']]", accountName);
driver.findElement(By.xpath(xpath)).click();
}
}