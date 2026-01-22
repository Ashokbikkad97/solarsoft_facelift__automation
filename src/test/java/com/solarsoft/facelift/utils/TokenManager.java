package com.solarsoft.facelift.utils;

import com.solarsoft.facelift.clients.AuthClient;
import io.restassured.response.Response;

public class TokenManager {

    private static String token;
    private static long expiryTime;

    public static String getToken() {

        if (token == null || System.currentTimeMillis() > expiryTime) {
            refreshToken();
        }
        return token;
    }

    private static void refreshToken() {

        Response res = AuthClient.getDealerToken();

        token = res.path("access_token");
        Integer expiresIn = res.path("expires_in");

        expiryTime = System.currentTimeMillis() + ((expiresIn - 60) * 1000);

        System.out.println("🔐 New token generated");
    }
}
