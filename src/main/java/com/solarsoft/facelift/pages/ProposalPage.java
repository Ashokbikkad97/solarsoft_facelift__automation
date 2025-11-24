package com.solarsoft.facelift.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class ProposalPage {
private WebDriver driver;


private By addProposalBtn = By.cssSelector("button[data-testid='add-proposal'], button[title='Add Proposal'], //button[contains(.,'Add Proposal')]");
private By proposalNameInput = By.cssSelector("input[name='proposalName'], input[id='proposalName']");
private By saveDraftBtn = By.cssSelector("button[data-testid='save-proposal'], button[title='Save']");
private By attachmentInput = By.cssSelector("input[type='file'][name='attachments'], input[data-testid='proposal-attachments']");
private By generatePlansBtn = By.cssSelector("button[data-testid='generate-plans'], button[title='Generate Plans'], //button[contains(.,'Generate Plans')]");
private By submitBtn = By.cssSelector("button[data-testid='submit-proposal'], button[title='Submit'], //button[normalize-space(.)='Submit']");


public ProposalPage(WebDriver driver) {
this.driver = driver;
}


public void clickAddProposal() {
driver.findElement(addProposalBtn).click();
}


public void fillBasicDetails(String propName) {
driver.findElement(proposalNameInput).clear();
driver.findElement(proposalNameInput).sendKeys(propName);
driver.findElement(saveDraftBtn).click();
}


public void uploadAttachment(String filePath) {
driver.findElement(attachmentInput).sendKeys(filePath);
}


public void clickGeneratePlans() {
driver.findElement(generatePlansBtn).click();
}


public void submitProposal() {
driver.findElement(submitBtn).click();
}
}