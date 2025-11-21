package com.solarsoft.facelift.pages;

import com.solarsoft.facelift.driver.DriverManager;
import com.solarsoft.facelift.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class DashboardPage {

    private WebDriver driver;

    // Global search input (id from HTML)
    @FindBy(id = "searchText")
    private WebElement globalSearchInput;

    // Dashboard cards container - each card has class "dashboard-card-data"
    @FindBy(css = "div.dashboard-card .dashboard-card-data")
    private List<WebElement> dashboardCards;

    // Table rows inside dashboard table
    @FindBy(css = "table.custom-data-table tbody tr")
    private List<WebElement> tableRows;

    // Pagination info (Showing X to Y of Z entries)
    @FindBy(css = "div.border_tbl_pagination .pagination-count p")
    private WebElement paginationText;

    public DashboardPage() {
        this.driver = DriverManager.getDriver();
        PageFactory.initElements(driver, this);
    }

    /**
     * Verify dashboard loaded by checking presence of global search input.
     */
    public boolean isDashboardLoaded() {
        try {
            return WaitUtils.waitForVisibility(globalSearchInput, 10).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Search using global search box and press Enter.
     */
    public void searchGlobal(String text) {
        WebElement input = WaitUtils.waitForVisibility(globalSearchInput, 10);
        input.clear();
        input.sendKeys(text);
        input.sendKeys(Keys.ENTER);
    }

    /**
     * Return number shown on a dashboard card by matching its label.
     * Example labels from HTML: "Documents not sent", "Orders ready to submit"
     *
     * @param labelPartial partial or full label text to match (case-insensitive)
     * @return the number string (like "2") or empty if not found
     */
    public String getCardValueByLabel(String labelPartial) {
        try {
            List<WebElement> cards = driver.findElements(By.cssSelector("div.dashboard-card .dashboard-card-data"));
            for (WebElement card : cards) {
                WebElement numberEl = null;
                WebElement labelEl = null;
                try {
                    numberEl = card.findElement(By.cssSelector(".dashboard-overview-number"));
                    labelEl = card.findElement(By.cssSelector(".dashboard-overview-label"));
                } catch (Exception ignore) {
                }
                if (numberEl != null && labelEl != null) {
                    String labelText = labelEl.getText().replace("\n", " ").trim();
                    if (labelText.toLowerCase().contains(labelPartial.toLowerCase())) {
                        return numberEl.getText().trim();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    /**
     * Click a dashboard card by matching its label text partially.
     * Useful to navigate to specific cards (e.g., "Documents not sent").
     *
     * @param labelPartial partial label to click
     * @return true if clicked, false if not found
     */
    public boolean clickCardByLabel(String labelPartial) {
        try {
            List<WebElement> cards = driver.findElements(By.cssSelector("div.dashboard-card .dashboard-card-data"));
            for (WebElement card : cards) {
                WebElement labelEl = null;
                try {
                    labelEl = card.findElement(By.cssSelector(".dashboard-overview-label"));
                } catch (Exception ignore) {
                }
                if (labelEl != null) {
                    String labelText = labelEl.getText().replace("\n", " ").trim();
                    if (labelText.toLowerCase().contains(labelPartial.toLowerCase())) {
                        WaitUtils.clickWhenClickable(card, 10);
                        return true;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Get number of rows currently displayed in dashboard table.
     */
    public int getTableRowsCount() {
        try {
            List<WebElement> rows = driver.findElements(By.cssSelector("table.custom-data-table tbody tr"));
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Return pagination info text like "Showing 1 to 2 of 2 entries"
     */
    public String getPaginationText() {
        try {
            return WaitUtils.waitForVisibility(paginationText, 5).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Open a proposal by its visible proposal number shown in the table (e.g., "034-0034-01-01").
     * This finds the anchor inside the table row whose text equals the given proposalNumber and clicks it.
     *
     * @param proposalNumber exact visible text of proposal in table
     * @return true if found & clicked, false otherwise
     */
    public boolean openProposalByProposalNumber(String proposalNumber) {
        try {
            List<WebElement> rows = driver.findElements(By.cssSelector("table.custom-data-table tbody tr"));
            for (WebElement r : rows) {
                try {
                    List<WebElement> anchors = r.findElements(By.cssSelector("a.text-decoration"));
                    for (WebElement a : anchors) {
                        if (a.getText() != null && a.getText().trim().equals(proposalNumber)) {
                            WaitUtils.clickWhenClickable(a, 10);
                            return true;
                        }
                    }
                } catch (Exception ignoreRow) {
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Open proposal by Account Rep name (visible link text in first column).
     *
     * @param accountRepName visible account rep text (partial match supported)
     * @return true if clicked
     */
    public boolean openProposalByAccountRepName(String accountRepName) {
        try {
            List<WebElement> rows = driver.findElements(By.cssSelector("table.custom-data-table tbody tr"));
            for (WebElement r : rows) {
                try {
                    WebElement firstColAnchor = r.findElement(By.cssSelector("td:nth-child(1) a.text-decoration"));
                    if (firstColAnchor != null) {
                        String text = firstColAnchor.getText().trim();
                        if (text.toLowerCase().contains(accountRepName.toLowerCase())) {
                            WaitUtils.clickWhenClickable(firstColAnchor, 10);
                            return true;
                        }
                    }
                } catch (Exception ignoreRow) {
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Click action menu (three-dot) for the row that contains given proposal number.
     *
     * @param proposalNumber visible proposal number to identify row
     * @return true if found and clicked
     */
    public boolean clickActionMenuForProposal(String proposalNumber) {
        try {
            List<WebElement> rows = driver.findElements(By.cssSelector("table.custom-data-table tbody tr"));
            for (WebElement r : rows) {
                try {
                    List<WebElement> anchors = r.findElements(By.cssSelector("a.text-decoration"));
                    boolean rowMatches = false;
                    for (WebElement a : anchors) {
                        if (a.getText() != null && a.getText().trim().equals(proposalNumber)) {
                            rowMatches = true;
                            break;
                        }
                    }
                    if (rowMatches) {
                        WebElement actionBtn = r.findElement(By.cssSelector("button.btn.list-drop-click"));
                        WaitUtils.clickWhenClickable(actionBtn, 10);
                        return true;
                    }
                } catch (Exception ignoreRow) {
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

} // end class
