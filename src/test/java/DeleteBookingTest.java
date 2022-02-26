import com.api.AuthTokenApi;
import com.api.CreateBookingApi;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import static com.api.BaseApi.BASE_URI;
import static com.api.CreateBookingApi.BOOKING_ENDPOINT;
import static com.api.GetBookingApi.getBookingById;
import static io.restassured.RestAssured.given;

@Slf4j
public class DeleteBookingTest {

    @Test @SneakyThrows
    public void verifyDeleteExistingBooking() {
        Number bookingId = CreateBookingApi.createBooking();
        String authToken = AuthTokenApi.getAuthToken();
        Response response = sendDeleteRequestAndFetchResponse(authToken, bookingId);
        Assert.assertEquals(HttpStatus.SC_CREATED,response.getStatusCode());
        Assert.assertEquals("Not able to delete booking.",
                (getBookingById(bookingId).getStatusCode()),HttpStatus.SC_NOT_FOUND);
        log.info("Successfully deleted booking with id: " +bookingId);
    }

    @Test
    public void verifyDeleteNonExistingBooking() {
        String authToken = AuthTokenApi.getAuthToken();
        Response response = sendDeleteRequestAndFetchResponse(authToken, 345609357);
        Assert.assertEquals("Booking does not exist.",
                HttpStatus.SC_METHOD_NOT_ALLOWED,response.getStatusCode());
    }

    @Test @SneakyThrows
    public void verifyUnauthorizedDeletionOfBooking() {
        Number bookingId = CreateBookingApi.createBooking();
        Response response = sendDeleteRequestAndFetchResponse("invalidToken123", bookingId);
        Assert.assertEquals("Deletion operation forbidden for unauthorized user.",
                HttpStatus.SC_FORBIDDEN,response.getStatusCode());
    }

    @Test @SneakyThrows
    public void verifyDeletionWithInvalidEndpoint() {
        Number bookingId = CreateBookingApi.createBooking();
        String authToken = AuthTokenApi.getAuthToken();
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .header("Cookie","token=" +authToken)
                .when()
                .delete(BASE_URI + "invalid_endpoint" +bookingId)
                .then()
                .extract().response();
        Assert.assertEquals("Not Found",HttpStatus.SC_NOT_FOUND,response.getStatusCode());
    }

    protected static Response sendDeleteRequestAndFetchResponse (String token, Number bookingId) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .header("Cookie","token=" +token)
                .when()
                .delete(BOOKING_ENDPOINT + "/" +bookingId)
                .then()
                .extract().response();
    }
}
