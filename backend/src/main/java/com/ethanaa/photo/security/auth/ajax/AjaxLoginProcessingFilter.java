package com.ethanaa.photo.security.auth.ajax;

import com.ethanaa.photo.security.model.LoginRequest;
import com.ethanaa.photo.security.model.exception.AuthMethodNotSupportedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private static Logger LOG = LoggerFactory.getLogger(AjaxLoginProcessingFilter.class);

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    private final ObjectMapper objectMapper;
    
    public AjaxLoginProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler, 
            AuthenticationFailureHandler failureHandler, ObjectMapper mapper) {

        super(defaultProcessUrl);

        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.objectMapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        if (!HttpMethod.POST.name().equals(request.getMethod())) {

            if(LOG.isDebugEnabled()) {
                LOG.debug("Authentication method not supported. Request method: " + request.getMethod());
            }

            throw new AuthMethodNotSupportedException("Authentication method not supported");
        }

        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        
        if (StringUtils.isEmpty(loginRequest.getUsername())) {
            throw new AuthenticationServiceException("Username not provided");
        }

        if (StringUtils.isEmpty(loginRequest.getPassword())) {
            throw new AuthenticationServiceException("Password not provided");
        }

        if (loginRequest.getApplications() == null ||
                loginRequest.getApplications().isEmpty() ||
                StringUtils.isEmpty(loginRequest.getApplications().get(0))) {
            throw new AuthenticationServiceException("No application name provided");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new ApplicationAwarePrincipal(loginRequest.getUsername(), loginRequest.getApplications())
                , loginRequest.getPassword());

        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
