package com.ethanaa.photo.security.controller.exception;


import com.ethanaa.photo.security.entity.Role;

public class RoleNotFoundException extends NotFoundException {

    public RoleNotFoundException(Object id) {

        super(Role.class, id);
    }
}
