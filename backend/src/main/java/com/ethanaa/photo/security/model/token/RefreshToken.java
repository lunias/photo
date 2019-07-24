package com.ethanaa.photo.security.model.token;


import com.ethanaa.photo.security.model.Scopes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class RefreshToken implements JwtToken {

    private Jws<Claims> claims;

    private RefreshToken(Jws<Claims> claims) {
        this.claims = claims;
    }

    public static Optional<RefreshToken> create(RawAccessJwtToken token, PublicKey publicKey,
                                                String applicationName, boolean logInvalidTokens) {

        Jws<Claims> claims = token.parseClaims(publicKey, logInvalidTokens);

        Map<String, List<String>> scopes = claims.getBody().get("scopes", Map.class);
        if (scopes == null
                || scopes.get(applicationName) == null
                || !scopes.get(applicationName).contains(Scopes.REFRESH_TOKEN.authority())) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(claims));
    }

    @Override
    public String getToken() {
        return null;
    }

    public Jws<Claims> getClaims() {
        return claims;
    }
    
    public String getJti() {
        return claims.getBody().getId();
    }
    
    public String getSubject() {
        return claims.getBody().getSubject();
    }
}
