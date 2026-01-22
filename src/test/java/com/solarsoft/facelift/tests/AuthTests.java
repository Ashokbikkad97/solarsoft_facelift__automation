package com.solarsoft.facelift.tests;

import com.solarsoft.facelift.clients.AuthClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTests {

    @Test(priority = 1)
    public void generateDealerToken() {

        Response response = AuthClient.getDealerToken();

        Assert.assertEquals(response.statusCode(), 200);

        String token = response.path("access_token");
        Assert.assertNotNull(token, "❌ Token is null!");

        System.out.println("✅ Dealer Token Generated: " + token);
    }
}
