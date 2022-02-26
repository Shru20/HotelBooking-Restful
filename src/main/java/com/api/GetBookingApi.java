package com.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class GetBookingApi extends BaseApi {

    public static Response getBookingById(Number id) {
        return given()
                .header("Accept", ContentType.JSON)
                .get(BASE_URI + "/" +id);
    }
}
