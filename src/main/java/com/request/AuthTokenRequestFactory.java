package com.request;

public class AuthTokenRequestFactory {

    public static AuthTokenRequest buildCreateTokenRequest() {
        return AuthTokenRequest.builder()
                .username("admin")
                .password("password123")
                .build();
    }
}
