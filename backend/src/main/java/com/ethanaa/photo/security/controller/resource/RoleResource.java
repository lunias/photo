package com.ethanaa.photo.security.controller.resource;

import org.springframework.hateoas.EntityModel;

public class RoleResource extends EntityModel<Long> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
