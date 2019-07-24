package com.ethanaa.photo.security.model.token;

public interface JwtToken {

    String HEADER_PARAM = "Authorization";

    String getToken();
}
