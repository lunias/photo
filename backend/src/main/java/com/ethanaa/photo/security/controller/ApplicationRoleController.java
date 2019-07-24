package com.ethanaa.photo.security.controller;


import com.ethanaa.photo.security.controller.assembler.ApplicationRoleAssembler;
import com.ethanaa.photo.security.controller.resource.ApplicationRoleResource;
import com.ethanaa.photo.security.entity.ApplicationRole;
import com.ethanaa.photo.security.service.ApplicationRoleService;
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
@RequestMapping("/api/applicationRoles")
public class ApplicationRoleController {

    private ApplicationRoleService applicationRoleService;
    private ApplicationRoleAssembler applicationRoleAssembler;

    @Autowired
    public ApplicationRoleController(ApplicationRoleService applicationRoleService, ApplicationRoleAssembler applicationRoleAssembler) {

        this.applicationRoleService = applicationRoleService;
        this.applicationRoleAssembler = applicationRoleAssembler;
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<PagedModel<ApplicationRoleResource>> getApplicationRoles(
            Pageable pageable, PagedResourcesAssembler<ApplicationRole> pagedAssembler) {

        Page<ApplicationRole> page = applicationRoleService.getAll(pageable);

        return ResponseEntity.ok(pagedAssembler.toModel(page, applicationRoleAssembler));
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ApplicationRoleResource> getApplicationRole(@PathVariable String id) {

        return ResponseEntity.ok(applicationRoleAssembler.toModel(applicationRoleService.get(id)));
    }

}
