import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;

import static com.api.CreateBookingApi.BOOKING_ENDPOINT;
import static io.restassured.RestAssured.given;

@Slf4j
public class GetBookingIdsTest {

    @Test
    public void verifyGetBookingIdsWithoutFilter() {
        Response response = sendGetRequestAndFetchResponse("");
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        JSONArray jsonArray = new JSONArray(response.asString());
        Assert.assertFalse(jsonArray.length() == 0);
        log.info("Total number of bookings : " +jsonArray.length());
    }

    @Test
    public void verifyGetBookingIdsWithSingleFilter() {
        Response response = sendGetRequestAndFetchResponse("?firstname=John");
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        JSONArray jsonArray = new JSONArray(response.asString());
        Assert.assertFalse("Total number of bookings with first name as John : " +jsonArray.length(),
                jsonArray.length() == 0);
    }

    @Test
    public void verifyGetBookingIdsWithMultipleFilters() {
        Response response = sendGetRequestAndFetchResponse
                ("?checkin=2020-06-07 & checkout=2020-06-09");
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        JSONArray jsonArray = new JSONArray(response.asString());
        Assert.assertFalse("Total number of bookings with checkin 2020-06-07 and checkout 2020-06-09 : "
                +jsonArray.length(),jsonArray.length() == 0);
    }

    @Test
    public void verifyGetBookingIdsWithInvalidFilter() {
        Response response = sendGetRequestAndFetchResponse("?location=Belgium");
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        JSONArray jsonArray = new JSONArray(response.asString());
        Assert.assertTrue("Incorrect results for an invalid location filter. Items found: " +jsonArray.length(),
                jsonArray.length() == 0);
    }

    @Test
    public void verifyGetBookingIdsWithInvalidEndpoint() {
        Response response = given()
                .when()
                .get(BOOKING_ENDPOINT + "invalid endpoint")
                .then()
                .extract().response();
        Assert.assertEquals("Endpoint Not Valid", HttpStatus.SC_NOT_FOUND,response.getStatusCode());
    }

    protected static Response sendGetRequestAndFetchResponse (String filter) {
        Response response = given()
                .when()
                .get(BOOKING_ENDPOINT + filter)
                .then()
                .extract().response();
        log.info("Required booking ids are: " +response);
        return response;
    }
}