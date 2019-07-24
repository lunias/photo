package com.ethanaa.photo.security.auth.jwt.verifier;

import com.ethanaa.photo.security.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenVerifier implements TokenVerifier {

    private RefreshTokenService refreshTokenService;

    @Autowired
    public RefreshTokenVerifier(RefreshTokenService refreshTokenService) {

        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public boolean verify(String subject, String jti) {

        return refreshTokenService.verify(subject, jti);
    }
}
