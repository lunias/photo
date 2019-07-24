package com.ethanaa.photo.security.model.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.Claims;

public final class AccessJwtToken implements JwtToken {

    private final String rawToken;

    @JsonIgnore
    private Claims claims;

    public AccessJwtToken(final String token, Claims claims) {

        this.rawToken = token;
        this.claims = claims;
    }

    @JsonCreator
    public AccessJwtToken(@JsonProperty("token") final String token) {

        this.rawToken = token;
    }

    public String getToken() {
        return this.rawToken;
    }

    public Claims getClaims() {
        return claims;
    }
}
