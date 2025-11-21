package com.solarsoft.facelift.tests;

import com.solarsoft.facelift.pages.ProposalListPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ActionMenuTests extends BaseTest {

    @Test(groups = {"Regression"})
    public void openEllipsisMenuForProposalAndSelectView() {
        ProposalListPage listPage = new ProposalListPage();
        Assert.assertTrue(listPage.isPageLoaded(), "Proposal list page should be loaded");

        // click ellipsis for a known proposal
        boolean clicked = listPage.clickEllipsisForProposal("034-0034-01-01"); // change as required
        Assert.assertTrue(clicked, "Ellipsis (action menu) should be clicked for the row");

        // choose "View Proposal" (example action)
        boolean actionSelected = listPage.selectActionFromEllipsisMenu("View Proposal");
        Assert.assertTrue(actionSelected, "Action 'View Proposal' should be selectable from ellipsis menu");
    }

    @Test(groups = {"Regression"})
    public void ellipsisMenuHasSendDocumentsAction() {
        ProposalListPage listPage = new ProposalListPage();
        Assert.assertTrue(listPage.isPageLoaded(), "Proposal list page should be loaded");

        boolean clicked = listPage.clickEllipsisForProposal("034-0034-01-01"); // change as needed
        Assert.assertTrue(clicked, "Clicked ellipsis");

        boolean sendDocs = listPage.selectActionFromEllipsisMenu("Send Documents");
        Assert.assertTrue(sendDocs, "Should find 'Send Documents' in action menu");
    }
}
