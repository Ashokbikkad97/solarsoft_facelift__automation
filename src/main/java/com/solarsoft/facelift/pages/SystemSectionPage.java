package com.solarsoft.facelift.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;


import java.util.List;


public class SystemSectionPage {
private WebDriver driver;


private By panelMfrSelect = By.cssSelector("select[name='panelMfr'], select[id='panelMfr']");
private By panelModelSelect = By.cssSelector("select[name='panelModel']");
private By inverterMfrSelect = By.cssSelector("select[name='inverterMfr']");
private By inverterModelSelect = By.cssSelector("select[name='inverterModel']");
private By addRowBtn = By.cssSelector("button[data-testid='add-array-row'], button[title='+ Row'], //button[contains(.,'+ Row')]");
private By arrayRows = By.cssSelector("div.array-row, div[data-testid='array-row']");
private By lastRowQty = By.cssSelector("div.array-row:last-child input[name='qty'], div[data-testid='array-row']:last-child input[name='qty']");
private By lastRowAzimuth = By.cssSelector("div.array-row:last-child input[name='azimuth']");
private By lastRowPitch = By.cssSelector("div.array-row:last-child input[name='pitch']");


public SystemSectionPage(WebDriver driver) {
this.driver = driver;
}


public void selectPanelManufacturer(String visibleText) {
Select s = new Select(driver.findElement(panelMfrSelect));
s.selectByVisibleText(visibleText);
}


public void selectPanelModel(String visibleText) {
Select s = new Select(driver.findElement(panelModelSelect));
s.selectByVisibleText(visibleText);
}


public void selectInverterManufacturer(String visibleText) {
Select s = new Select(driver.findElement(inverterMfrSelect));
s.selectByVisibleText(visibleText);
}


public void selectInverterModel(String visibleText) {
Select s = new Select(driver.findElement(inverterModelSelect));
s.selectByVisibleText(visibleText);
}


public void addArrayRow() {
driver.findElement(addRowBtn).click();
}


public void fillLastRowNumbers(String qty, String azimuth, String pitch) {
WebElement qtyEl = driver.findElement(lastRowQty);
qtyEl.clear(); qtyEl.sendKeys(qty);
WebElement az = driver.findElement(lastRowAzimuth);
az.clear(); az.sendKeys(azimuth);
WebElement p = driver.findElement(lastRowPitch);
p.clear(); p.sendKeys(pitch);
}


public int getArrayRowCount() {
List rows = driver.findElements(arrayRows);
return rows.size();
}
}