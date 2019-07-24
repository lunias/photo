package com.ethanaa.photo.security.model.token;


import com.ethanaa.photo.security.model.exception.JwtExpiredTokenException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

import java.security.PublicKey;

public class RawAccessJwtToken implements JwtToken {

    private static Logger logger = LoggerFactory.getLogger(RawAccessJwtToken.class);
            
    private String token;
    
    public RawAccessJwtToken(String token) {

        this.token = token;
    }

    public Jws<Claims> parseClaims(PublicKey publicKey, boolean logInvalidTokens) {
        try {
            return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(this.token);

        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {

            if (logInvalidTokens) logger.error("Invalid JWT Token", ex);
            throw new BadCredentialsException("Invalid JWT token: ", ex);

        } catch (ExpiredJwtException expiredEx) {

            if (logInvalidTokens) logger.info("JWT Token is expired", expiredEx);
            throw new JwtExpiredTokenException(this, "JWT Token expired", expiredEx);
        }
    }

    @Override
    public String getToken() {

        return token;
    }
}
