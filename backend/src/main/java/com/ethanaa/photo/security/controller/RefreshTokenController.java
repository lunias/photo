package com.ethanaa.photo.security.controller;

import com.ethanaa.photo.security.AuthProperties;
import com.ethanaa.photo.security.auth.jwt.extractor.TokenExtractor;
import com.ethanaa.photo.security.model.UserContext;
import com.ethanaa.photo.security.model.exception.InvalidJwtToken;
import com.ethanaa.photo.security.model.token.JwtToken;
import com.ethanaa.photo.security.model.token.RawAccessJwtToken;
import com.ethanaa.photo.security.model.token.RefreshToken;
import com.ethanaa.photo.security.entity.Role;
import com.ethanaa.photo.security.entity.User;
import com.ethanaa.photo.security.service.UserLookupService;
import com.ethanaa.photo.security.auth.jwt.JwtTokenFactory;
import com.ethanaa.photo.security.auth.jwt.verifier.TokenVerifier;
import com.ethanaa.photo.config.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
public class RefreshTokenController {

    @Autowired private JwtTokenFactory tokenFactory;
    @Autowired private UserLookupService userLookupService;
    @Autowired private TokenVerifier tokenVerifier;
    @Autowired private TokenExtractor tokenExtractor;
    @Autowired private PublicKey publicKey;
    @Autowired private AuthProperties properties;
    
    @RequestMapping(value="${photo.auth.server.refreshTokenEndpoint}", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<JwtToken> refreshToken(HttpServletRequest request)
            throws IOException, ServletException {

        String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.JWT_TOKEN_HEADER_PARAM));
        
        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(
                rawToken,
                publicKey,
                properties.getResource().getApplicationName(),
                properties.getResource().getLogInvalidTokens())
                .orElseThrow(InvalidJwtToken::new);

        String jti = refreshToken.getJti();
        if (!tokenVerifier.verify(refreshToken.getSubject(), jti)) {
            throw new InvalidJwtToken("invalid jti");
        }

        String subject = refreshToken.getSubject();
        Map<String, List<String>> scopes = refreshToken.getClaims().getBody()
                .get("scopes", Map.class);

        User user = userLookupService.getByUsername(subject).orElseThrow(
                () -> new UsernameNotFoundException("User not found: " + subject));

        Map<String, List<Role>> roles = user.getRolesForApplications(new ArrayList<>(scopes.keySet()));
        if (roles.isEmpty()) throw new InsufficientAuthenticationException("User has no roles assigned");

        Map<String, List<GrantedAuthority>> authorities = new HashMap<>();
        for (Map.Entry<String, List<Role>> entry : roles.entrySet()) {

            List<GrantedAuthority> grantedAuthorities = entry.getValue().stream()
                    .map(r -> new SimpleGrantedAuthority(r.getName()))
                    .collect(Collectors.toList());

            authorities.put(entry.getKey(), grantedAuthorities);
        }

        UserContext userContext = UserContext.create(user.getUsername(), authorities);

        return ResponseEntity.ok(tokenFactory.createAccessJwtToken(userContext));
    }
}
