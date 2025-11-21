package com.solarsoft.facelift.pages;

import com.solarsoft.facelift.driver.DriverManager;
import com.solarsoft.facelift.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ProposalListPage {

    private WebDriver driver;

    // Table rows (re-queried inside methods for fresh DOM)
    @FindBy(css = "table.custom-data-table tbody")
    private WebElement tableBody;

    // Page header or some unique element - fallback locator
    @FindBy(css = "h1, .page-title")
    private WebElement pageHeader;

    public ProposalListPage() {
        this.driver = DriverManager.getDriver();
        PageFactory.initElements(driver, this);
    }

    public boolean isPageLoaded() {
        try {
            // If there is a header or visible table
            if (pageHeader != null) {
                return WaitUtils.waitForVisibility(pageHeader, 8).isDisplayed();
            } else {
                return WaitUtils.waitForVisibility(tableBody, 8).isDisplayed();
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Open a proposal from the list by its visible proposal number (exact match).
     * Returns true if clicked.
     */
    public boolean openProposalByNumber(String proposalNumber) {
        try {
            List<WebElement> rows = driver.findElements(By.cssSelector("table.custom-data-table tbody tr"));
            for (WebElement r : rows) {
                try {
                    // anchors with text-decoration contain links to proposal
                    List<WebElement> anchors = r.findElements(By.cssSelector("a.text-decoration"));
                    for (WebElement a : anchors) {
                        if (a.getText() != null && a.getText().trim().equals(proposalNumber)) {
                            WaitUtils.clickWhenClickable(a, 10);
                            return true;
                        }
                    }
                } catch (Exception ignoreRow) {}
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Click the action (ellipsis) button for a given proposal number row.
     * Returns true if found and clicked.
     */
    public boolean clickEllipsisForProposal(String proposalNumber) {
        try {
            List<WebElement> rows = driver.findElements(By.cssSelector("table.custom-data-table tbody tr"));
            for (WebElement r : rows) {
                try {
                    List<WebElement> anchors = r.findElements(By.cssSelector("a.text-decoration"));
                    boolean match = false;
                    for (WebElement a : anchors) {
                        if (a.getText() != null && a.getText().trim().equals(proposalNumber)) {
                            match = true;
                            break;
                        }
                    }
                    if (match) {
                        WebElement actionBtn = r.findElement(By.cssSelector("button.btn.list-drop-click"));
                        WaitUtils.clickWhenClickable(actionBtn, 8);
                        return true;
                    }
                } catch (Exception ignoreRow) {}
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * From an open action menu (after clicking ellipsis), click an action by its visible text.
     * This is generic: looks for visible dropdown/menu items and matches text.
     */
    public boolean selectActionFromEllipsisMenu(String actionVisibleText) {
        try {
            // Assuming the action menu appears somewhere in DOM. Look for visible dropdown items.
            List<WebElement> items = driver.findElements(By.cssSelector(".dropdown-menu a, .action-menu li, .ellipsis-menu li, .action-list li, .dropdown-item"));
            for (WebElement item : items) {
                try {
                    if (item.isDisplayed() && item.getText() != null && item.getText().trim().equalsIgnoreCase(actionVisibleText.trim())) {
                        WaitUtils.clickWhenClickable(item, 8);
                        return true;
                    }
                } catch (Exception ignoreItem) {}
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Returns number of rows present in the proposals table
     */
    public int getProposalsCount() {
        try {
            List<WebElement> rows = driver.findElements(By.cssSelector("table.custom-data-table tbody tr"));
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }
}
