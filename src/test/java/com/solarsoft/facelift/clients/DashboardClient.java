package com.solarsoft.facelift.clients;

import com.solarsoft.facelift.base.BaseAPI;
import com.solarsoft.facelift.utils.TokenManager;
import io.restassured.response.Response;

public class DashboardClient extends BaseAPI {

    // ✅ Already existing
    public static Response getOrdersReadyToSubmit() {

        String payload = "{\n" +
                "  \"criterias\": [\n" +
                "    {\n" +
                "      \"key\": \"tab\",\n" +
                "      \"value\": \"ordersReadyToSubmit\",\n" +
                "      \"operation\": \"EQUAL\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"page\": 0,\n" +
                "  \"size\": 50,\n" +
                "  \"orders\": []\n" +
                "}";

        return request()
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .body(payload)
                .post("/sales/v1/salesCustomers/sites/proposal/dashboard")
                .then()
                .extract()
                .response();
    }

    // 🔥 NEW — Documents Not Sent
    public static Response getDocumentsNotSent() {

        String payload = "{\n" +
                "  \"criterias\": [\n" +
                "    {\n" +
                "      \"key\": \"tab\",\n" +
                "      \"value\": \"documentsNotSent\",\n" +
                "      \"operation\": \"EQUAL\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"page\": 0,\n" +
                "  \"size\": 50,\n" +
                "  \"orders\": []\n" +
                "}";

        return request()
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .body(payload)
                .post("/sales/v1/salesCustomers/sites/proposal/dashboard")
                .then()
                .extract()
                .response();
    }
}
