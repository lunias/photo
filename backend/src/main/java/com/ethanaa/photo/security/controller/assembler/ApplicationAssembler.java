package com.ethanaa.photo.security.controller.assembler;

import com.ethanaa.photo.security.controller.ApplicationController;
import com.ethanaa.photo.security.controller.resource.ApplicationResource;
import com.ethanaa.photo.security.entity.Application;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class ApplicationAssembler implements RepresentationModelAssembler<Application, ApplicationResource> {

    @Override
    public ApplicationResource toModel(Application entity) {

        ApplicationResource resource = new ApplicationResource();

        BeanUtils.copyProperties(entity, resource);

        resource.add(linkTo(
                methodOn(ApplicationController.class).getApplication(entity.getId())).withSelfRel());

        resource.add(linkTo(
                methodOn(ApplicationController.class).getApplicationRoles(entity.getId())).withRel("roles"));

        return resource;
    }
}
