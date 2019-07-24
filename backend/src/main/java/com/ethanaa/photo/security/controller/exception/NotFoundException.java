package com.ethanaa.photo.security.controller.exception;


public class NotFoundException extends RuntimeException {

    public NotFoundException(Class entityClass, Object id) {

        super("Could not find " + entityClass.getSimpleName() + " (" + id + ")");
    }
}
