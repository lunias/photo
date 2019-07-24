package com.ethanaa.photo.security.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LoginRequest {

    private String username;
    private String password;
    private List<String> applications;

    @JsonCreator
    public LoginRequest(@JsonProperty("username") String username,
                        @JsonProperty("password") String password,
                        @JsonProperty("applications") List<String> applications) {

        this.username = username;
        this.password = password;
        this.applications = applications;
    }

    public String getUsername() {

        return username;
    }

    public String getPassword() {

        return password;
    }

    public List<String> getApplications() {

        return applications;
    }
}
