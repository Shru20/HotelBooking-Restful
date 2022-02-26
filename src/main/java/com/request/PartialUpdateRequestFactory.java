package com.request;

public class PartialUpdateRequestFactory {

    public static String buildPartialUpdateRequestWithNameDetails() {
        return "{\n" +
                "    \"firstname\" : \"Shruthy\",\n" +
                "    \"lastname\" : \"Nair\"\n" +
                "}";
    }

    public static String buildPartialUpdateRequestWithPriceDetails() {
        return " {\n" +
                "    \"totalprice\": 1500,\n" +
                "    \"depositpaid\": false\n" +
                "}";
    }

    public static String buildPartialUpdateRequestWithBookingDates() {
        return "{\n" +
                "    \"bookingdates\": {\n" +
                "    \"checkin\": \"2023-01-09\",\n" +
                "    \"checkout\": \"2023-02-01\"\n" +
                "  }\n" +
                "}";
    }

    public static String buildPartialUpdateRequestWithInvalidFormat() {
        return "{\n" +
                "    \"firstname\": Shruthy,\n" +
                "    \"totalprice\": \"765\"\n" +
                "}";
    }

    public static String buildPartialUpdateRequestWithAdditionalNeeds() {
        return "{\n" +
                "   \"additionalneeds\" : \"Meals\"\n" +
                "}";
    }
}
