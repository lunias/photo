package com.ethanaa.photo.security.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {

    private String token;
    private String refreshToken;

    @JsonCreator
    public LoginResponse(@JsonProperty("token") String token,
                         @JsonProperty("refreshToken") String refreshToken) {

        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
