import com.api.AuthTokenApi;
import com.api.CreateBookingApi;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static com.api.BaseApi.BASE_URI;
import static com.api.CreateBookingApi.BOOKING_ENDPOINT;
import static com.request.PartialUpdateRequestFactory.*;
import static com.request.PartialUpdateRequestFactory.buildPartialUpdateRequestWithNameDetails;
import static io.restassured.RestAssured.given;

@Slf4j
public class PartialUpdateBookingTest {

    @Test
    public void verifyPartialUpdateBookingForNameDetails () {
        Response response = sendPatchRequestAndFetchResponse(buildPartialUpdateRequestWithNameDetails());
        Assert.assertEquals(HttpStatus.SC_OK,response.getStatusCode());
        Assert.assertEquals("Failed to update first name. Expected : Shruthy but Actual : "
                +response.jsonPath().getString("firstname"),"Shruthy",
                response.jsonPath().getString("firstname"));
        Assert.assertEquals("Failed to update name name. Expected : Nair but Actual : "
                +response.jsonPath().getString("lastname"),"Nair",
                response.jsonPath().getString("lastname"));
    }

    @Test
    public void verifyPartialUpdateBookingForPriceDetails () {
        Response response = sendPatchRequestAndFetchResponse(buildPartialUpdateRequestWithPriceDetails());
        Assert.assertEquals(HttpStatus.SC_OK,response.getStatusCode());
        Assert.assertEquals("Failed to update total price. Expected : 1500 but Actual : "
                +response.jsonPath().getInt("totalprice"),1500,
                response.jsonPath().getInt("totalprice"));
        Assert.assertEquals("Failed to update deposit paid. Expected : false but Actual : "
                +response.jsonPath().getBoolean("depositpaid"),false,
                response.jsonPath().getBoolean("depositpaid"));
    }

    @Test
    public void verifyPartialUpdateBookingForBookingDates()  {
        Response response = sendPatchRequestAndFetchResponse(buildPartialUpdateRequestWithBookingDates());
        Assert.assertEquals(HttpStatus.SC_OK,response.getStatusCode());
        JSONObject bookingDates = new JSONObject(response.asString()).getJSONObject("bookingdates");
        Assert.assertEquals("Failed to update checkin. Expected : 2023-01-09 but Actual : "
                        +bookingDates.getString("checkin"),"2023-01-09",
                bookingDates.getString("checkin"));
        Assert.assertEquals("Failed to update checkout. Expected : 2023-02-01 but Actual : "
                        +bookingDates.getString("checkout"),"2023-02-01",
                bookingDates.getString("checkout"));
    }

    @Test
    public void verifyPartialUpdateBookingForAdditionalNeeds () {
        Response response = sendPatchRequestAndFetchResponse(buildPartialUpdateRequestWithAdditionalNeeds());
        Assert.assertEquals(HttpStatus.SC_OK,response.getStatusCode());
        Assert.assertEquals("Failed to update additional needs. Expected : Meals but Actual : "
                        +response.jsonPath().getString("additionalneeds"),"Meals",
                response.jsonPath().getString("additionalneeds"));
    }

    @Test
    public void verifyUnsuccessfulPartialUpdateBookingWithInvalidRequest () {
        Response response = sendPatchRequestAndFetchResponse(buildPartialUpdateRequestWithInvalidFormat());
        Assert.assertEquals("Invalid request format.",HttpStatus.SC_BAD_REQUEST,response.getStatusCode());
    }

    @Test @SneakyThrows
    public void verifyUnauthorizedPartialUpdateBooking () {
        Number bookingId = CreateBookingApi.createBooking();
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Accept","application/json")
                .header("Cookie","token=" + "invalidToken123")
                .and()
                .body(buildPartialUpdateRequestWithNameDetails())
                .when()
                .patch(BOOKING_ENDPOINT +"/" +bookingId)
                .then()
                .extract().response();
        Assert.assertEquals("Partial Booking Update forbidden for unauthorized user",
                HttpStatus.SC_FORBIDDEN,response.getStatusCode());
    }

    @Test @SneakyThrows
    public void verifyPartialUpdateBookingWithIncorrectAcceptHeader () {
        Number bookingId = CreateBookingApi.createBooking();
        String authToken = AuthTokenApi.getAuthToken();
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Accept","test")
                .header("Cookie","token=" +authToken)
                .and()
                .body(buildPartialUpdateRequestWithNameDetails())
                .when()
                .patch(BOOKING_ENDPOINT +"/" +bookingId)
                .then()
                .extract().response();
        Assert.assertEquals("Partial Booking Update failed for incorrect accept header." +
                        "Internal Server Error", HttpStatus.SC_INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @SneakyThrows
    protected static Response sendPatchRequestAndFetchResponse (String partialUpdateRequest) {
        Number bookingId = CreateBookingApi.createBooking();
        String authToken = AuthTokenApi.getAuthToken();
        log.info("Patch URI is : " +BASE_URI +"booking/" +bookingId);
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Accept","application/json")
                .header("Cookie","token=" +authToken)
                .and()
                .body(partialUpdateRequest)
                .when()
                .patch(BOOKING_ENDPOINT +"/" +bookingId)
                .then()
                .extract().response();
        log.info("Updated Booking Details are: "+response.getBody().asPrettyString());
        return response;
    }
}

