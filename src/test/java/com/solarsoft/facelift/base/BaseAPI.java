package com.solarsoft.facelift.base;

import com.solarsoft.facelift.config.ConfigReader;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class BaseAPI {

    protected static RequestSpecification request() {
        return RestAssured
                .given()
                .baseUri(ConfigReader.get("api.base.url"))
                .relaxedHTTPSValidation()
                .header("Content-Type", "application/json");
    }
}
