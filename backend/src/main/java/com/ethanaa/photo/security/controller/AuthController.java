package com.ethanaa.photo.security.controller;

import com.ethanaa.photo.security.auth.jwt.JwtAuthenticationToken;
import com.ethanaa.photo.security.auth.jwt.extractor.TokenExtractor;
import com.ethanaa.photo.security.model.UserContext;
import com.ethanaa.photo.security.model.UserResource;
import com.ethanaa.photo.security.model.token.JwtToken;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

    private TokenExtractor tokenExtractor;

    public AuthController(TokenExtractor tokenExtractor) {

        this.tokenExtractor = tokenExtractor;
    }

    @RequestMapping(value = "/api/users/byUsername/{username}", method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserResource> getUser(@PathVariable String username, HttpServletRequest request) {

        String tokenPayload = tokenExtractor.extract(
                request.getHeader(JwtToken.HEADER_PARAM));

        return null; //authServerClient.getUser(username, tokenPayload);
    }

    @RequestMapping(value = "${photo.auth.resource.userContextEndpoint}", method = RequestMethod.GET)
    public ResponseEntity<UserContext> getUserContext(JwtAuthenticationToken token) {

        return ResponseEntity.ok((UserContext) token.getPrincipal());
    }
}
