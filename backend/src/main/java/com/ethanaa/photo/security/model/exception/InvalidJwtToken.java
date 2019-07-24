package com.ethanaa.photo.security.model.exception;


public class InvalidJwtToken extends RuntimeException {

    private static final long serialVersionUID = -294671188037098603L;

    public InvalidJwtToken() {
        //
    }

    public InvalidJwtToken(String message) {

        super(message);
    }
}
