package com.api;

import com.request.BookingRequest;
import com.request.BookingRequestFactory;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import static io.restassured.RestAssured.given;

@Slf4j
public class CreateBookingApi extends BaseApi {

    public static final String BOOKING_ENDPOINT = BASE_URI + "booking";

    @SneakyThrows
    public static Number createBooking()  {
        BookingRequest bookingRequest = BookingRequestFactory.buildCreateBookingRequest();
        log.info ("Create Booking URI is: " +BOOKING_ENDPOINT);
        log.info("Booking Request body is: " +bookingRequest.toString());
        Response response = given()
                .contentType(ContentType.JSON)
                .body(bookingRequest)
                .when()
                .post(BOOKING_ENDPOINT)
                .then().extract().response();
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertTrue((response.asString().contains("bookingid")));
        log.info("Booking Response Details are: "+response.getBody().asPrettyString());
        return response.jsonPath().getInt("bookingid");
    }
}
