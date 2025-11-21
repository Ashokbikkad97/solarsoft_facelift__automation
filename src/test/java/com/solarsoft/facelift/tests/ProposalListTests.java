package com.solarsoft.facelift.tests;

import com.solarsoft.facelift.pages.DashboardPage;
import com.solarsoft.facelift.pages.ProposalListPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProposalListTests extends BaseTest {

    /**
     * Navigate from Dashboard to Proposal List (by clicking side nav or card),
     * then assert proposal list page loaded.
     */
    @Test(groups = {"Regression"})
    public void openProposalListFromDashboard() {
        DashboardPage dash = new DashboardPage();
        Assert.assertTrue(dash.isDashboardLoaded(), "Dashboard should be loaded");

        // open Proposals via left menu - ideally we would have a page object for menu,
        // but navigate by URL if known. Using click on card that navigates to proposals is optional.
        driver.get(driver.getCurrentUrl().replace("/", "") + "/proposalList"); // fallback: navigate to proposalList
        ProposalListPage listPage = new ProposalListPage();
        Assert.assertTrue(listPage.isPageLoaded(), "Proposal list page should be loaded");
    }

    /**
     * Open a proposal by known proposal number
     */
    @Test(groups = {"Regression"})
    public void openProposalByNumber() {
        ProposalListPage listPage = new ProposalListPage();
        Assert.assertTrue(listPage.isPageLoaded(), "Proposal list page should be loaded");

        boolean opened = listPage.openProposalByNumber("034-0034-01-01"); // update value if needed
        Assert.assertTrue(opened, "Should open proposal by exact proposal number");
    }
}
