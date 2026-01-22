package com.solarsoft.facelift.clients;

import com.solarsoft.facelift.base.BaseAPI;
import com.solarsoft.facelift.config.ConfigReader;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient extends BaseAPI {

    public static Response getDealerToken() {

        return given()
                .baseUri(ConfigReader.get("api.base.url"))
                .auth()
                .preemptive()
                .basic(
                        ConfigReader.get("api.client.id"),
                        ConfigReader.get("api.client.secret")
                )
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", ConfigReader.get("api.username"))
                .formParam("password", ConfigReader.get("api.password"))
                .formParam("grant_type", "password")
                .post("/oauthservice/oauth/token")
                .then()
                .log().all()
                .extract()
                .response();
    }
}
