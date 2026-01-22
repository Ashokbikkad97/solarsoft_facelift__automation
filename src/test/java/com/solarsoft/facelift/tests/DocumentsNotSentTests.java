package com.solarsoft.facelift.tests;

import com.solarsoft.facelift.clients.DashboardClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DocumentsNotSentTests {

	@Test(priority = 3)
	public void verifyDocumentsNotSentList() {

	    Response response = DashboardClient.getDocumentsNotSent();

	    Assert.assertEquals(response.statusCode(), 200, "Status code should be 200");

	    int size = response.jsonPath().getList("data.content").size();
	    System.out.println("📄 Documents Not Sent Count = " + size);

	    if (size > 0) {
	        String proposalStatus = response.jsonPath().getString("data.content[0].proposalStatus");
	        String salesPacketStatus = response.jsonPath().getString("data.content[0].salesPacketStatus");
	        Boolean enableOrderCreated = response.jsonPath().getBoolean("data.content[0].enableOrderCreated");

	        Assert.assertEquals(proposalStatus, "Accepted", "Proposal should be Accepted");
	        Assert.assertEquals(salesPacketStatus, "NOT_SENT", "Sales packet should be NOT_SENT");
	        Assert.assertFalse(enableOrderCreated, "enableOrderCreated should be false");
	    }
	}

}
