package com.ethanaa.photo.security.service;

import com.ethanaa.photo.security.entity.RefreshToken;
import com.ethanaa.photo.security.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RefreshTokenService {

    private RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {

        this.refreshTokenRepository = refreshTokenRepository;
    }

    public boolean verify(String username, String jti) {

        RefreshToken refreshToken = refreshTokenRepository.findByUsername(username);

        if (refreshToken == null) {
            return false;
        }

        return Objects.equals(refreshToken.getJti(), jti) && refreshToken.isValid();
    }

    public RefreshToken save(RefreshToken refreshToken) {

        return refreshTokenRepository.save(refreshToken);
    }
}
