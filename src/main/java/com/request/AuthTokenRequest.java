package com.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
public class AuthTokenRequest {

        @JsonProperty("username")
        private String username;

        @JsonProperty("password")
        private String password;
}
