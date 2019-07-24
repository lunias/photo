package com.ethanaa.photo.security.service;


import com.ethanaa.photo.security.AuthProperties;
import com.ethanaa.photo.security.controller.exception.ApplicationRoleNotFoundException;
import com.ethanaa.photo.security.controller.exception.UserNotFoundException;
import com.ethanaa.photo.security.entity.Application;
import com.ethanaa.photo.security.entity.ApplicationRole;
import com.ethanaa.photo.security.entity.Role;
import com.ethanaa.photo.security.entity.User;
import com.ethanaa.photo.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserLookupService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private ApplicationRoleService applicationRoleService;
    private ApplicationService applicationService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthProperties authProperties;

    @Autowired
    public UserService(UserRepository userRepository,
                       ApplicationRoleService applicationRoleService,
                       ApplicationService applicationService,
                       RoleService roleService,
                       PasswordEncoder passwordEncoder,
                       AuthProperties authProperties) {

        this.userRepository = userRepository;
        this.applicationRoleService = applicationRoleService;
        this.applicationService = applicationService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.authProperties = authProperties;
    }

    public User create(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public Iterable<User> create(Iterable<User> users) {

        users.forEach(u -> u.setPassword(
                passwordEncoder.encode(u.getPassword())));

        return userRepository.saveAll(users);
    }

    public User update(User user) {

        return userRepository.save(user);
    }

    public Iterable<User> update(Iterable<User> users) {

        return userRepository.saveAll(users);
    }

    public Page<User> getAll(Pageable pageRequest) {

        return userRepository.findAll(pageRequest);
    }

    public User get(String id) {

        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) throw new UserNotFoundException(id);

        return user.get();
    }

    @Override
    public Optional<User> getByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    public Optional<User> loginMock(String username, String password) {

        final User user = getByUsername(username).orElse(new User());

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstname("mockFirst");
        user.setLastname("mockLast");
        user.setEmail("mock@mock.com");

        List<ApplicationRole> userApplicationRoles = new ArrayList<>();

        ApplicationRole photoRead = null;
        ApplicationRole photoAdmin = null;
        try {
            photoRead = applicationRoleService.getByApplicationNameAndRoleName("photo", "read");
            photoAdmin = applicationRoleService.getByApplicationNameAndRoleName("photo", "admin");
        } catch (ApplicationRoleNotFoundException arnfe) {
            Application photoApplication = applicationService.create(new Application("photo"));
            Role readRole = roleService.create(new Role("read"));
            Role adminRole = roleService.create(new Role("admin"));
            photoRead = applicationRoleService.create(new ApplicationRole(photoApplication, readRole));
            photoAdmin = applicationRoleService.create(new ApplicationRole(photoApplication, adminRole));
        }

        userApplicationRoles.add(photoRead);
        userApplicationRoles.add(photoAdmin);

        user.setApplicationRoles(userApplicationRoles);

        return Optional.of(update(user));
    }

    public Map<String, List<Role>> getApplicationRoles(String id) {

        return get(id).getApplicationRoles().stream()
                .collect(Collectors.groupingBy(ar -> ar.getApplication().getName(),
                        Collectors.mapping(ApplicationRole::getRole, Collectors.toList())));
    }
}
