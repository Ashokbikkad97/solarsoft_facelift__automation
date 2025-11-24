package com.solarsoft.facelift.tests;

import com.solarsoft.facelift.tests.*;
import com.solarsoft.facelift.pages.AccountPage;
import com.solarsoft.facelift.pages.AccountsPage;
import com.solarsoft.facelift.pages.ProposalPage;
import com.solarsoft.facelift.pages.SitePage;
import com.solarsoft.facelift.pages.SystemSectionPage;
import com.solarsoft.facelift.pages.DashboardPage;
import com.solarsoft.facelift.pages.LoginPage;
import com.solarsoft.facelift.config.ConfigReader;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Instant;

/**
 * Updated DealerTests that align with your existing DashboardPage (no-arg PageFactory style)
 * and with page classes that accept WebDriver in constructor (new AccountsPage(driver), etc.)
 */
public class DealerTests extends BaseTest {

    @Test(priority = 1, groups = {"Smoke", "Regression"})
    public void TC01_dealerLoginAndDashboard() {
        LoginPage login = new LoginPage();
        DashboardPage dash = new DashboardPage(); // uses PageFactory + DriverManager internally

        // login using config properties helper
        login.loginWithValidUser();

        // use the dashboard method you provided
        Assert.assertTrue(dash.isDashboardLoaded(),
                "Dashboard should be loaded and global search visible after login");
    }

    @Test(priority = 2, groups = {"Smoke", "Regression"})
    public void TC02_addAccountHappyPath() {
        LoginPage login = new LoginPage();
        DashboardPage dash = new DashboardPage();
        AccountsPage accounts = new AccountsPage(driver); // constructor with WebDriver

        login.loginWithValidUser();

        // navigate to accounts page explicitly (avoids relying on a missing goToAccounts() method)
        driver.get(ConfigReader.get("base.url") + "/accounts");

        accounts.clickAddAccount();

        String accountName = "AutoTestAccount_" + Instant.now().getEpochSecond();
        accounts.createAccount(accountName);

        Assert.assertTrue(accounts.isAccountPresentInList(accountName),
                "Newly created account should appear in accounts list");
    }

    @Test(priority = 3, groups = {"Regression"})
    public void TC03_addAccountRequiredFieldsValidation() {
        LoginPage login = new LoginPage();
        AccountsPage accounts = new AccountsPage(driver);

        login.loginWithValidUser();
        driver.get(ConfigReader.get("base.url") + "/accounts");

        accounts.clickAddAccount();
        accounts.createAccount(""); // empty -> expect validation

        boolean hasValidation = !driver.findElements(
                By.xpath("//*[contains(text(),'required') or contains(text(),'Required')]"))
                .isEmpty();

        Assert.assertTrue(hasValidation, "Validation message should appear for required fields");
    }

    @Test(priority = 4, groups = {"Regression"})
    public void TC04_addSiteHappyPath() {
        LoginPage login = new LoginPage();
        AccountsPage accounts = new AccountsPage(driver);
        AccountPage accountPage = new AccountPage(driver);
        SitePage sitePage = new SitePage(driver);

        login.loginWithValidUser();
        driver.get(ConfigReader.get("base.url") + "/accounts");

        String accountName = "AutoSiteAccount_" + Instant.now().getEpochSecond();
        accounts.clickAddAccount();
        accounts.createAccount(accountName);

        accounts.openAccountFromList(accountName);

        accountPage.clickAddSite();

        String siteName = "Site_" + Instant.now().getEpochSecond();
        sitePage.fillSite(siteName, "123 Test Street", "411014", "4500");

        boolean sitePresent = !driver.findElements(
                By.xpath("//tr[.//td[normalize-space(text())='" + siteName + "']]"))
                .isEmpty();

        Assert.assertTrue(sitePresent, "Site should appear under account after save");
    }

    @Test(priority = 5, groups = {"Regression"})
    public void TC05_addSiteValidation() {
        LoginPage login = new LoginPage();
        AccountsPage accounts = new AccountsPage(driver);
        AccountPage accountPage = new AccountPage(driver);
        SitePage sitePage = new SitePage(driver);

        login.loginWithValidUser();
        driver.get(ConfigReader.get("base.url") + "/accounts");

        String accountName = "AutoSiteAccountVal_" + Instant.now().getEpochSecond();
        accounts.clickAddAccount();
        accounts.createAccount(accountName);

        accounts.openAccountFromList(accountName);

        accountPage.clickAddSite();

        String siteName = "InvalidSite_" + Instant.now().getEpochSecond();
        sitePage.fillSite(siteName, "Some Address", "411014", "abcd"); // invalid usage

        boolean usageValidation = !driver.findElements(
                By.xpath("//*[contains(text(),'numeric') or contains(text(),'Invalid')]"))
                .isEmpty();

        Assert.assertTrue(usageValidation, "Usage field should show validation for non-numeric input");
    }

    @Test(priority = 6, groups = {"Smoke", "Regression"})
    public void TC06_createProposalBasic() {
        LoginPage login = new LoginPage();
        AccountsPage accounts = new AccountsPage(driver);
        AccountPage accountPage = new AccountPage(driver);
        SitePage sitePage = new SitePage(driver);
        ProposalPage proposal = new ProposalPage(driver);

        login.loginWithValidUser();
        driver.get(ConfigReader.get("base.url") + "/accounts");

        String accountName = "PropAcc_" + Instant.now().getEpochSecond();
        accounts.clickAddAccount();
        accounts.createAccount(accountName);

        accounts.openAccountFromList(accountName);

        accountPage.clickAddSite();

        String siteName = "PropSite_" + Instant.now().getEpochSecond();
        sitePage.fillSite(siteName, "Address 1", "411014", "3000");

        proposal.clickAddProposal();

        String propName = "AutoProposal_" + Instant.now().getEpochSecond();
        proposal.fillBasicDetails(propName);

        boolean propPresent = !driver.findElements(
                By.xpath("//tr[.//td[normalize-space(text())='" + propName + "']]"))
                .isEmpty();

        Assert.assertTrue(propPresent, "Proposal should be saved as draft and visible");
    }

    @Test(priority = 7, groups = {"Regression"})
    public void TC07_systemAddArrayAndSelectComponents() throws InterruptedException {
        LoginPage login = new LoginPage();
        AccountsPage accounts = new AccountsPage(driver);
        AccountPage accountPage = new AccountPage(driver);
        SitePage sitePage = new SitePage(driver);
        ProposalPage proposal = new ProposalPage(driver);
        SystemSectionPage system = new SystemSectionPage(driver);

        login.loginWithValidUser();
        driver.get(ConfigReader.get("base.url") + "/accounts");

        String accountName = "SysAcc_" + Instant.now().getEpochSecond();
        accounts.clickAddAccount();
        accounts.createAccount(accountName);

        accounts.openAccountFromList(accountName);

        accountPage.clickAddSite();

        String siteName = "SysSite_" + Instant.now().getEpochSecond();
        sitePage.fillSite(siteName, "Address XYZ", "411014", "5000");

        proposal.clickAddProposal();

        String propName = "SysProp_" + Instant.now().getEpochSecond();
        proposal.fillBasicDetails(propName);

        system.selectPanelManufacturer("REC");
        system.selectPanelModel("REC 410AA Pure");
        system.selectInverterManufacturer("Tesla");
        system.selectInverterModel("Tesla INV: 153800-xx-y AB");

        int before = system.getArrayRowCount();
        system.addArrayRow();
        Thread.sleep(600);
        system.fillLastRowNumbers("20", "270", "20");

        Assert.assertTrue(system.getArrayRowCount() > before, "+Row should add new array row");
    }

    @Test(priority = 8, groups = {"Regression"})
    public void TC08_uploadAttachment() {
        LoginPage login = new LoginPage();
        AccountsPage accounts = new AccountsPage(driver);
        AccountPage accountPage = new AccountPage(driver);
        SitePage sitePage = new SitePage(driver);
        ProposalPage proposal = new ProposalPage(driver);

        login.loginWithValidUser();
        driver.get(ConfigReader.get("base.url") + "/accounts");

        String accountName = "AttAcc_" + Instant.now().getEpochSecond();
        accounts.clickAddAccount();
        accounts.createAccount(accountName);

        accounts.openAccountFromList(accountName);

        accountPage.clickAddSite();

        String siteName = "AttSite_" + Instant.now().getEpochSecond();
        sitePage.fillSite(siteName, "Address 2", "411014", "2100");

        proposal.clickAddProposal();

        String propName = "AttProp_" + Instant.now().getEpochSecond();
        proposal.fillBasicDetails(propName);

        String filePath = "src/test/resources/9b2ef687-179a-4f15-933f-e3d17da5771a.png";
        proposal.uploadAttachment(filePath);

        boolean attached = !driver.findElements(
                By.xpath("//*[contains(text(),'png') or contains(text(),'image')]"))
                .isEmpty();

        Assert.assertTrue(attached, "Attachment should upload");
    }

    @Test(priority = 9, groups = {"Regression"})
    public void TC09_generatePlans() throws InterruptedException {
        LoginPage login = new LoginPage();
        AccountsPage accounts = new AccountsPage(driver);
        AccountPage accountPage = new AccountPage(driver);
        SitePage sitePage = new SitePage(driver);
        ProposalPage proposal = new ProposalPage(driver);

        login.loginWithValidUser();
        driver.get(ConfigReader.get("base.url") + "/accounts");

        String acc = "GenAcc_" + Instant.now().getEpochSecond();
        accounts.clickAddAccount();
        accounts.createAccount(acc);

        accounts.openAccountFromList(acc);

        accountPage.clickAddSite();

        String site = "GenSite_" + Instant.now().getEpochSecond();
        sitePage.fillSite(site, "Address 5", "411014", "3200");

        proposal.clickAddProposal();

        String prop = "GenProp_" + Instant.now().getEpochSecond();
        proposal.fillBasicDetails(prop);

        proposal.clickGeneratePlans();
        Thread.sleep(1000);

        boolean generated = !driver.findElements(
                By.xpath("//*[contains(text(),'Plan') or contains(text(),'Download')]"))
                .isEmpty();

        Assert.assertTrue(generated, "Plans should generate");
    }

    @Test(priority = 10, groups = {"Regression"})
    public void TC10_submitProposal() throws InterruptedException {
        LoginPage login = new LoginPage();
        AccountsPage accounts = new AccountsPage(driver);
        AccountPage accountPage = new AccountPage(driver);
        SitePage sitePage = new SitePage(driver);
        ProposalPage proposal = new ProposalPage(driver);

        login.loginWithValidUser();
        driver.get(ConfigReader.get("base.url") + "/accounts");

        String acc = "SubAcc_" + Instant.now().getEpochSecond();
        accounts.clickAddAccount();
        accounts.createAccount(acc);

        accounts.openAccountFromList(acc);

        accountPage.clickAddSite();

        String site = "SubSite_" + Instant.now().getEpochSecond();
        sitePage.fillSite(site, "Address Z", "411014", "2500");

        proposal.clickAddProposal();

        String prop = "SubmitProp_" + Instant.now().getEpochSecond();
        proposal.fillBasicDetails(prop);

        proposal.submitProposal();
        Thread.sleep(800);

        boolean statusChanged = !driver.findElements(
                By.xpath("//td[contains(text(),'Submitted') or contains(text(),'Pending')]"))
                .isEmpty();

        Assert.assertTrue(statusChanged, "Proposal status should update after submit");
    }

    @Test(priority = 11, groups = {"Regression"})
    public void TC11_roleRestriction() {
        LoginPage login = new LoginPage();

        login.loginWithValidUser();

        driver.get(ConfigReader.get("base.url") + "/drm/dashboard");

        boolean denied = !driver.findElements(
                By.xpath("//*[contains(text(),'Unauthorized') or contains(text(),'not allowed')]"))
                .isEmpty();

        Assert.assertTrue(denied, "Dealer should NOT access DRM pages");
    }

    @Test(priority = 12, groups = {"Regression"})
    public void TC12_persistenceAfterLogout() {
        LoginPage login = new LoginPage();
        AccountsPage accounts = new AccountsPage(driver);

        login.loginWithValidUser();
        driver.get(ConfigReader.get("base.url") + "/accounts");

        String acc = "PersistAcc_" + Instant.now().getEpochSecond();
        accounts.clickAddAccount();
        accounts.createAccount(acc);

        // logout - using a generic button locator; update if your app differs
        driver.findElement(By.xpath("//button[contains(.,'Logout') or contains(.,'Sign out')]")).click();

        // login again using config props
        login.loginWithValidUser();
        driver.get(ConfigReader.get("base.url") + "/accounts");

        boolean present = accounts.isAccountPresentInList(acc);

        Assert.assertTrue(present, "Data should persist after logout-login");
    }
}
