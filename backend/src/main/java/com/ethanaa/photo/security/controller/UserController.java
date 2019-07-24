package com.ethanaa.photo.security.controller;

import com.ethanaa.photo.security.auth.jwt.JwtAuthenticationToken;
import com.ethanaa.photo.security.model.UserContext;
import com.ethanaa.photo.security.model.UserResource;
import com.ethanaa.photo.security.controller.assembler.RoleAssembler;
import com.ethanaa.photo.security.controller.assembler.UserAssembler;
import com.ethanaa.photo.security.controller.exception.UserNotFoundException;
import com.ethanaa.photo.security.controller.resource.RoleResource;
import com.ethanaa.photo.security.entity.Role;
import com.ethanaa.photo.security.entity.User;
import com.ethanaa.photo.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;
    private UserAssembler userAssembler;
    private RoleAssembler roleAssembler;

    @Autowired
    public UserController(UserService userService,
                          UserAssembler userAssembler,
                          RoleAssembler roleAssembler) {

        this.userService = userService;
        this.userAssembler = userAssembler;
        this.roleAssembler = roleAssembler;
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<PagedModel<UserResource>> getUsers(
            Pageable pageable, PagedResourcesAssembler<User> pagedAssembler) {

        Page<User> page = userService.getAll(pageable);

        return ResponseEntity.ok(pagedAssembler.toModel(page, userAssembler));
    }

    // TODO permissionEvaluator?
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<UserResource> getUser(@PathVariable String id) {

        User user = userService.get(id);

        return ResponseEntity.ok(userAssembler.toModel(user));
    }

    // TODO permissionEvaluator?
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/applicationRoles")
    public ResponseEntity<Map<String, CollectionModel<RoleResource>>> getApplicationRoles(@PathVariable String id) {

        Map<String, CollectionModel<RoleResource>> applicationRoles = new LinkedHashMap<>();
        for (Map.Entry<String, List<Role>> entry : userService.getApplicationRoles(id).entrySet()) {
            applicationRoles.put(entry.getKey(), roleAssembler.toCollectionModel(entry.getValue()));
        }

        return ResponseEntity.ok(applicationRoles);
    }

    @PreAuthorize("hasAuthority('admin') || #username == authentication.principal.username")
    @RequestMapping(method = RequestMethod.GET, value = "/byUsername/{username}")
    public ResponseEntity<UserResource> getUserByUsername(@PathVariable String username) {

        User user = userService.getByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username));

        return ResponseEntity.ok(userAssembler.toModel(user));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/me")
    public ResponseEntity<UserContext> getMe(JwtAuthenticationToken token) {

        return ResponseEntity.ok((UserContext) token.getPrincipal());
    }
}
