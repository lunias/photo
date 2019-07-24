package com.ethanaa.photo.security.controller;


import com.ethanaa.photo.security.controller.assembler.ApplicationAssembler;
import com.ethanaa.photo.security.controller.assembler.RoleAssembler;
import com.ethanaa.photo.security.controller.resource.ApplicationResource;
import com.ethanaa.photo.security.controller.resource.RoleResource;
import com.ethanaa.photo.security.entity.Application;
import com.ethanaa.photo.security.service.ApplicationService;
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

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private ApplicationService applicationService;
    private ApplicationAssembler applicationAssembler;
    private RoleAssembler roleAssembler;

    @Autowired
    public ApplicationController(ApplicationService applicationService,
                                 ApplicationAssembler applicationAssembler,
                                 RoleAssembler roleAssembler) {

        this.applicationService = applicationService;
        this.applicationAssembler = applicationAssembler;
        this.roleAssembler = roleAssembler;
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<PagedModel<ApplicationResource>> getApplications(
            Pageable pageable, PagedResourcesAssembler<Application> pagedAssembler) {

        Page<Application> page = applicationService.getAll(pageable);

        return ResponseEntity.ok(pagedAssembler.toModel(page, applicationAssembler));
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ApplicationResource> getApplication(@PathVariable String id) {

        return ResponseEntity.ok(applicationAssembler.toModel(applicationService.get(id)));
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/roles")
    public ResponseEntity<CollectionModel<RoleResource>> getApplicationRoles(@PathVariable String id) {

        return ResponseEntity.ok(roleAssembler.toCollectionModel(applicationService.getRoles(id)));
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(method = RequestMethod.GET, value = "/byName/{name}")
    public ResponseEntity<ApplicationResource> getApplicationByName(@PathVariable String name) {

        return ResponseEntity.ok(applicationAssembler.toModel(applicationService.getByName(name)));
    }
}
