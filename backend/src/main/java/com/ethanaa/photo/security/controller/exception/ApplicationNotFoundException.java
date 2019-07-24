package com.ethanaa.photo.security.controller.exception;


import com.ethanaa.photo.security.entity.Application;

public class ApplicationNotFoundException extends NotFoundException {

    public ApplicationNotFoundException(Object id) {

        super(Application.class, id);
    }
}
