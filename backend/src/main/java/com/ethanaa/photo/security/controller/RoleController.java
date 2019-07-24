package com.ethanaa.photo.security.controller;

import com.ethanaa.photo.security.controller.assembler.RoleAssembler;
import com.ethanaa.photo.security.controller.resource.RoleResource;
import com.ethanaa.photo.security.entity.Role;
import com.ethanaa.photo.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private RoleService roleService;
    private RoleAssembler roleAssembler;

    @Autowired
    public RoleController(RoleService roleService, RoleAssembler roleAssembler) {

        this.roleService = roleService;
        this.roleAssembler = roleAssembler;
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<PagedModel<RoleResource>> getRoles(
            Pageable pageable, PagedResourcesAssembler<Role> pagedAssembler) {

        Page<Role> page = roleService.getAll(pageable);

        return ResponseEntity.ok(pagedAssembler.toModel(page, roleAssembler));
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<RoleResource> getRole(@PathVariable Long id) {

        return ResponseEntity.ok(roleAssembler.toModel(roleService.get(id)));
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(method = RequestMethod.GET, value = "/byName/{name}")
    public ResponseEntity<RoleResource> getRoleByName(@PathVariable String name) {

        return ResponseEntity.ok(roleAssembler.toModel(roleService.getByName(name)));
    }
}
