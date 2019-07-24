package com.ethanaa.photo.security.auth.jwt.verifier;


public interface TokenVerifier {

    boolean verify(String subject, String jti);
}
