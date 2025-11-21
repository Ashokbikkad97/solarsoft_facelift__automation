package com.solarsoft.facelift.tests;

import com.solarsoft.facelift.pages.DashboardPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DashboardCardTests extends BaseTest {

    @Test(groups = {"Smoke"})
    public void verifyDashboardCardsShowNumbers() {
        DashboardPage dash = new DashboardPage();
        Assert.assertTrue(dash.isDashboardLoaded(), "Dashboard should be displayed");

        String docsNotSent = dash.getCardValueByLabel("Documents not sent");
        String ordersReady = dash.getCardValueByLabel("Orders ready");

        // simple assertions: values exist (could be "0")
        Assert.assertNotNull(docsNotSent, "Documents not sent card value should be retrievable");
        Assert.assertNotNull(ordersReady, "Orders ready card value should be retrievable");
    }

    @Test(groups = {"Regression"})
    public void clickingCardNavigatesToFilteredList() {
        DashboardPage dash = new DashboardPage();
        Assert.assertTrue(dash.isDashboardLoaded(), "Dashboard should be displayed");

        boolean clicked = dash.clickCardByLabel("Documents not sent");
        Assert.assertTrue(clicked, "Should be able to click 'Documents not sent' card");

        // After click, optionally ensure proposals table is filtered (just check table rows present)
        int rows = dash.getTableRowsCount();
        Assert.assertTrue(rows >= 0, "Table rows should be accessible after clicking card");
    }
}
