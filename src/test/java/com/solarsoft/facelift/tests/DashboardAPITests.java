package com.solarsoft.facelift.tests;

import com.solarsoft.facelift.clients.DashboardClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DashboardAPITests {

    @Test(priority = 2)
    public void verifyOrdersReadyToSubmit() {

        Response response = DashboardClient.getOrdersReadyToSubmit();

        Assert.assertEquals(response.statusCode(), 200);

        int count = response.path("data.numberOfElements");
        Assert.assertTrue(count >= 0, "❌ Orders count invalid");

        System.out.println("✅ Orders Ready To Submit Count = " + count);
    }
}
