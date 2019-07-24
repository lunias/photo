package com.ethanaa.photo.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


public class UserContext {

    private final String username;
    private final Map<String, List<GrantedAuthority>> authorities;

    private UserContext(String username, Map<String, List<GrantedAuthority>> authorities) {

        this.username = username;
        this.authorities = authorities;
    }
    
    public static UserContext create(String username, Map<String, List<GrantedAuthority>> authorities) {

        if (StringUtils.isEmpty(username))
            throw new IllegalArgumentException("Username is blank: " + username);

        return new UserContext(username, authorities);
    }

    public String getUsername() {

        return username;
    }

    public Map<String, List<GrantedAuthority>> getAuthorities() {

        return authorities;
    }

    public List<GrantedAuthority> getAuthoritiesForApplication(String applicationName) {

        return authorities.get(applicationName);
    }
}
