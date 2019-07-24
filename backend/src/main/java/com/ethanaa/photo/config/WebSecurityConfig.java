package com.ethanaa.photo.config;

import com.ethanaa.photo.security.AuthProperties;
import com.ethanaa.photo.security.auth.RestAuthenticationEntryPoint;
import com.ethanaa.photo.security.auth.SkipPathRequestMatcher;
import com.ethanaa.photo.security.auth.ajax.AjaxLoginProcessingFilter;
import com.ethanaa.photo.security.auth.jwt.JwtAuthenticationProvider;
import com.ethanaa.photo.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import com.ethanaa.photo.security.auth.jwt.extractor.TokenExtractor;
import com.ethanaa.photo.security.auth.ajax.AjaxAuthenticationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String JWT_TOKEN_HEADER_PARAM = "Authorization";
    public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";
    
    @Autowired private RestAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired private AuthenticationSuccessHandler successHandler;
    @Autowired private AuthenticationFailureHandler failureHandler;
    @Autowired private AjaxAuthenticationProvider ajaxAuthenticationProvider;
    @Autowired private JwtAuthenticationProvider jwtAuthenticationProvider;
    
    @Autowired private TokenExtractor tokenExtractor;
    
    @Autowired private AuthenticationManager authenticationManager;
    
    @Autowired @Qualifier("objectMapper") private ObjectMapper objectMapper;

    @Autowired private AuthProperties properties;

    @Value("${server.ssl.key-store:}")
    private String sslKeyStore;

    protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter() throws Exception {

        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(
                properties.getServer().getTokenEndpoint(),
                successHandler, failureHandler, objectMapper);

        filter.setAuthenticationManager(this.authenticationManager);

        return filter;
    }
    
    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {

        List<String> pathsToSkip = Arrays.asList(properties.getServer().getTokenEndpoint(),
                properties.getServer().getRefreshTokenEndpoint());

        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENTRY_POINT);
        JwtTokenAuthenticationProcessingFilter filter 
            = new JwtTokenAuthenticationProcessingFilter(
                    failureHandler, tokenExtractor, matcher);

        filter.setAuthenticationManager(this.authenticationManager);

        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(ajaxAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if (!StringUtils.isEmpty(sslKeyStore)) {
            http
                    .requiresChannel()
                    .anyRequest().requiresSecure();
        }

        http
        .csrf().disable() // We don't need CSRF for JWT based authentication
        .headers().frameOptions().sameOrigin() // Allow for H2 Console
        .and()
            .exceptionHandling()
            .authenticationEntryPoint(this.authenticationEntryPoint)
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
                .antMatchers(properties.getServer().getTokenEndpoint()).permitAll() // Login end-point
                .antMatchers(properties.getServer().getRefreshTokenEndpoint()).permitAll() // Token refresh end-point
                .antMatchers("/manage/**").permitAll()
                .antMatchers("/h2").permitAll() // H2 Console Dash-board - only for testing
        .and()
            .authorizeRequests()
                .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated() // Protected API End-points
        .and()
            .addFilterBefore(buildAjaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
