package com.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.Date;

@Builder(toBuilder = true)
@Getter
@Setter
public class BookingDates {
    @JsonProperty("checkin")
    private Date checkin;

    @JsonProperty("checkout")
    private Date checkout;
}
