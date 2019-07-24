package com.ethanaa.photo.security.controller.exception;


import com.ethanaa.photo.security.entity.ApplicationRole;

public class ApplicationRoleNotFoundException extends NotFoundException {

    public ApplicationRoleNotFoundException(Object id) {

        super(ApplicationRole.class, id);
    }
}
