package com.ethanaa.photo.security.controller.assembler;

import com.ethanaa.photo.security.controller.RoleController;
import com.ethanaa.photo.security.controller.resource.RoleResource;
import com.ethanaa.photo.security.entity.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RoleAssembler implements RepresentationModelAssembler<Role, RoleResource> {

    @Override
    public RoleResource toModel(Role entity) {

        RoleResource resource = new RoleResource();

        BeanUtils.copyProperties(entity, resource);

        resource.add(linkTo(
                methodOn(RoleController.class).getRole(entity.getId())).withSelfRel());

        return resource;
    }
}
