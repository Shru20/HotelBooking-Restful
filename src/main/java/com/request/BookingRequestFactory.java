package com.request;

import lombok.SneakyThrows;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class BookingRequestFactory {

    public static BookingRequest buildCreateBookingRequest() throws ParseException {
        return BookingRequest.builder()
                .firstname("John")
                .lastname("Morgan")
                .totalprice(1000)
                .depositpaid(true)
                .bookingdates(buildBookingDates())
                .additionalneeds("Sight-Seeing")
                .build();
    }

    @SneakyThrows
    public static BookingDates buildBookingDates() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("India"));
        String cin = "7-Jun-2020";
        String cout = "9-Jun-2020";
        Date cinDate = formatter.parse(cin);
        Date coutDate = formatter.parse(cout);
        return BookingDates.builder()
                .checkin(cinDate)
                .checkout(coutDate)
                .build();
    }
}
