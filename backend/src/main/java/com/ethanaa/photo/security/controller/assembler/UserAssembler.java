package com.ethanaa.photo.security.controller.assembler;

import com.ethanaa.photo.security.model.UserResource;
import com.ethanaa.photo.security.controller.UserController;
import com.ethanaa.photo.security.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler implements RepresentationModelAssembler<User, UserResource> {

    @Override
    public UserResource toModel(User entity) {

        UserResource resource = new UserResource();

        BeanUtils.copyProperties(entity, resource, "password");

        resource.add(linkTo(
                methodOn(UserController.class).getUser(entity.getId())).withSelfRel());

        resource.add(linkTo(
                methodOn(UserController.class).getApplicationRoles(entity.getId())).withRel("applicationRoles"));

        return resource;
    }
}
