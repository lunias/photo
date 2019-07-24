package com.ethanaa.photo.security.auth.jwt;

import com.ethanaa.photo.security.AuthProperties;
import com.ethanaa.photo.security.model.Scopes;
import com.ethanaa.photo.security.model.UserContext;
import com.ethanaa.photo.security.model.token.AccessJwtToken;
import com.ethanaa.photo.security.model.token.JwtToken;
import com.ethanaa.photo.security.entity.RefreshToken;
import com.ethanaa.photo.security.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenFactory {

    private final AuthProperties properties;
    private final PrivateKey privateKey;

    private RefreshTokenService refreshTokenService;

    @Autowired
    public JwtTokenFactory(PrivateKey privateKey, AuthProperties properties,
                           RefreshTokenService refreshTokenService) {

        this.properties = properties;
        this.privateKey = privateKey;

        this.refreshTokenService = refreshTokenService;
    }

    public AccessJwtToken createAccessJwtToken(UserContext userContext) {

        if (StringUtils.isEmpty(userContext.getUsername()))
            throw new IllegalArgumentException("Cannot create JWT Token without username");

        if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty()) 
            throw new IllegalArgumentException("User doesn't have any privileges");

        Map<String, List<String>> scopes = new HashMap<>();
        for (Map.Entry<String, List<GrantedAuthority>> entry : userContext.getAuthorities().entrySet()) {

            List<String> authorities = entry.getValue().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            scopes.put(entry.getKey(), authorities);
        }

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put("scopes", scopes);

        LocalDateTime currentTime = LocalDateTime.now();
        
        String token = Jwts.builder()
          .setClaims(claims)
          .setIssuer(properties.getResource().getApplicationName())
          .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
          .setExpiration(Date.from(currentTime
              .plusMinutes(properties.getServer().getTokenValidMinutes())
              .atZone(ZoneId.systemDefault()).toInstant()))
          .signWith(SignatureAlgorithm.RS256, privateKey)
        .compact();

        return new AccessJwtToken(token, claims);
    }

    public JwtToken createRefreshToken(UserContext userContext) {

        if (StringUtils.isEmpty(userContext.getUsername()))
            throw new IllegalArgumentException("Cannot create JWT Token without username");

        LocalDateTime currentTime = LocalDateTime.now();
        String applicationName = properties.getResource().getApplicationName();

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put("scopes", new HashMap<String, List<String>>() {
            {
                for (String application : userContext.getAuthorities().keySet()) {
                    if (application.equals(applicationName)) {
                        put(applicationName, Arrays.asList(Scopes.REFRESH_TOKEN.authority()));
                    } else {
                        put(application, new ArrayList<>());
                    }
                }
            }
        });

        RefreshToken refreshToken = new RefreshToken(userContext.getUsername());

        String token = Jwts.builder()
          .setClaims(claims)
          .setIssuer(applicationName)
          .setId(refreshToken.getJti())
          .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
          .setExpiration(Date.from(currentTime
              .plusMinutes(properties.getServer().getRefreshTokenValidMinutes())
              .atZone(ZoneId.systemDefault()).toInstant()))
          .signWith(SignatureAlgorithm.RS256, privateKey)
        .compact();

        refreshTokenService.save(refreshToken);

        return new AccessJwtToken(token, claims);
    }
}
