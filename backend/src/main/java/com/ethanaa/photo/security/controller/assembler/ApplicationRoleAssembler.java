package com.ethanaa.photo.security.controller.assembler;


import com.ethanaa.photo.security.controller.ApplicationController;
import com.ethanaa.photo.security.controller.ApplicationRoleController;
import com.ethanaa.photo.security.controller.RoleController;
import com.ethanaa.photo.security.controller.resource.ApplicationRoleResource;
import com.ethanaa.photo.security.entity.ApplicationRole;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class ApplicationRoleAssembler implements RepresentationModelAssembler<ApplicationRole, ApplicationRoleResource> {

    @Override
    public ApplicationRoleResource toModel(ApplicationRole entity) {

        ApplicationRoleResource resource = new ApplicationRoleResource();

        resource.setApplication(entity.getApplication().getName());
        resource.setRole(entity.getRole().getName());

        resource.add(linkTo(
                methodOn(ApplicationRoleController.class).getApplicationRole(entity.getId())).withSelfRel());

        resource.add(linkTo(methodOn(ApplicationController.class).getApplication(entity.getApplication().getId())).withRel("application"));
        resource.add(linkTo(methodOn(RoleController.class).getRole(entity.getRole().getId())).withRel("role"));

        return resource;
    }
}
