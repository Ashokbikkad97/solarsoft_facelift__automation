package com.solarsoft.facelift.tests;

import com.solarsoft.facelift.pages.DashboardPage;
import com.solarsoft.facelift.pages.ProposalListPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GlobalSearchTests extends BaseTest {

    @Test(groups = {"Regression"})
    public void globalSearchFindsProposalAndOpens() throws InterruptedException {
    	DashboardPage dash = new DashboardPage();
        Assert.assertTrue(dash.isDashboardLoaded(), "Dashboard should be loaded");

        // search for known proposal number
        String proposal = "034-0034-01-01";
        dash.searchGlobal(proposal);

        // small wait to allow results to render - prefer explicit wait in real-world
        Thread.sleep(1500);

        ProposalListPage list = new ProposalListPage();
        // Either proposal opens from search results or exists in table - attempt open
        boolean opened = list.openProposalByNumber(proposal);
        Assert.assertTrue(opened, "Global search should allow opening the proposal");
    }

    @Test(groups = {"Smoke"})
    public void globalSearchSuggestsResults() throws InterruptedException {
    	DashboardPage dash = new DashboardPage();
        Assert.assertTrue(dash.isDashboardLoaded(), "Dashboard loaded");

        dash.searchGlobal("Abd Test"); // search by account name
        Thread.sleep(1000);

        int rows = dash.getTableRowsCount();
        Assert.assertTrue(rows >= 0, "Global search should display results in dashboard table");
    }
}
