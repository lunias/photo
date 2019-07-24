package com.ethanaa.photo.security.auth.ajax;

import com.ethanaa.photo.security.AuthProperties;
import com.ethanaa.photo.security.auth.ajax.ApplicationAwarePrincipal;
import com.ethanaa.photo.security.model.UserContext;
import com.ethanaa.photo.security.entity.Role;
import com.ethanaa.photo.security.entity.User;
import com.ethanaa.photo.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final AuthProperties properties;

    @Autowired
    public AjaxAuthenticationProvider(final UserService userService,
                                      final AuthProperties properties) {

        this.userService = userService;
        this.properties = properties;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Assert.notNull(authentication, "No authentication data provided");

        ApplicationAwarePrincipal principal = (ApplicationAwarePrincipal) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        List<String> applications = principal.getApplications();

        User user = userService.loginMock(principal.getUsername(), password)
                .orElseThrow(() -> new BadCredentialsException("Authentication Failed. Username or password not valid."));

        Map<String, List<Role>> roles = user.getRolesForApplications(applications);
        if (roles.isEmpty()) {
            throw new InsufficientAuthenticationException("User has no roles assigned");
        }

        String applicationName = properties.getResource().getApplicationName();

        Map<String, List<GrantedAuthority>> authorities = new HashMap<>();
        for (Map.Entry<String, List<Role>> entry : roles.entrySet()) {

            List<GrantedAuthority> grantedAuthorities = entry.getValue().stream()
                    .map(r -> new SimpleGrantedAuthority(r.getName()))
                    .collect(Collectors.toList());

            authorities.put(entry.getKey(), grantedAuthorities);
        }
        
        UserContext userContext = UserContext.create(user.getUsername(), authorities);
        
        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthoritiesForApplication(applicationName));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
