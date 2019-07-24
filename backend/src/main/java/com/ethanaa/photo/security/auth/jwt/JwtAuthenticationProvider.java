package com.ethanaa.photo.security.auth.jwt;


import com.ethanaa.photo.security.AuthProperties;
import com.ethanaa.photo.security.model.UserContext;
import com.ethanaa.photo.security.model.exception.InvalidJwtToken;
import com.ethanaa.photo.security.model.token.RawAccessJwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final PublicKey publicKey;
    private final AuthProperties properties;
    
    @Autowired
    public JwtAuthenticationProvider(PublicKey publicKey, AuthProperties properties) {

        this.publicKey = publicKey;
        this.properties = properties;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(publicKey, properties.getResource().getLogInvalidTokens());

        String subject = jwsClaims.getBody().getSubject();
        Map<String, List<String>> scopes = jwsClaims.getBody().get("scopes", Map.class);

        String applicationName = properties.getResource().getApplicationName();

        if (!scopes.keySet().contains(applicationName)) {
            throw new InvalidJwtToken("token not supported by resource");
        }

        Map<String, List<GrantedAuthority>> authorities = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : scopes.entrySet()) {

            List<GrantedAuthority> grantedAuthorities = entry.getValue().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            authorities.put(entry.getKey(), grantedAuthorities);
        }
        
        UserContext context = UserContext.create(subject, authorities);
        
        return new JwtAuthenticationToken(context, context.getAuthoritiesForApplication(applicationName));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
