import com.api.AuthTokenApi;
import com.api.CreateBookingApi;
import io.restassured.response.ValidatableResponse;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.Test;

import static com.api.CreateBookingApi.BOOKING_ENDPOINT;
import static com.api.GetBookingApi.getBookingById;
import static com.request.PartialUpdateRequestFactory.buildPartialUpdateRequestWithNameDetails;
import static com.request.PartialUpdateRequestFactory.buildPartialUpdateRequestWithPriceDetails;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class NonFunctionalTests {

    @Test
    public void ValidateResponseTimeForSuccessfulGetRequest() {
        Number bookingId = CreateBookingApi.createBooking();
        ValidatableResponse valRes = getBookingById(bookingId).then();
        valRes.time(Matchers.lessThan(1500L));
    }

    @Test
    public void ValidateResponseTimeForSuccessfulDeleteRequest() {
        Number bookingId = CreateBookingApi.createBooking();
        String authToken = AuthTokenApi.getAuthToken();
        ValidatableResponse valRes  = DeleteBookingTest.
                sendDeleteRequestAndFetchResponse(authToken, bookingId).then();
        valRes.time(Matchers.lessThan(1500L));
    }

    @Test
    public void ValidateResponseTimeForSuccessfulPatchRequest() {
        ValidatableResponse valRes = PartialUpdateBookingTest.
                sendPatchRequestAndFetchResponse(buildPartialUpdateRequestWithPriceDetails()).then();
        valRes.time(Matchers.lessThan(1500L));
    }

    @Test @SneakyThrows
    public void validateJsonSchemaForPartialUpdate() {
        Number bookingId = CreateBookingApi.createBooking();
        String authToken = AuthTokenApi.getAuthToken();
        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" +authToken)
                .and()
                .body(buildPartialUpdateRequestWithNameDetails())
                .when()
                .patch(BOOKING_ENDPOINT + "/" + bookingId)
                .then().log().all().body(matchesJsonSchemaInClasspath("jsonPartialUpdateResSchemaValidator.json"));
    }
}
