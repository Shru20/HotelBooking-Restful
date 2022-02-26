package com.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.request.AuthTokenRequestFactory.buildCreateTokenRequest;
import static io.restassured.RestAssured.given;

public class AuthTokenApi extends BaseApi {

    public static final String AUTH_ENDPOINT = "/auth";

    public static String getAuthToken(){
        Response response =  given().
                baseUri(BASE_URI).
                contentType(ContentType.JSON).
                body(buildCreateTokenRequest()).
                when().
                post(AUTH_ENDPOINT).
                then().
                log().all().
                extract().response();
        return response.jsonPath().getString("token");
    }
}
