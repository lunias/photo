package com.ethanaa.photo.security.controller.exception;


import com.ethanaa.photo.security.entity.User;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(Object id) {

        super(User.class, id);
    }
}
