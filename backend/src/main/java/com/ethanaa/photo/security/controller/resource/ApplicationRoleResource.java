package com.ethanaa.photo.security.controller.resource;


import org.springframework.hateoas.EntityModel;

public class ApplicationRoleResource extends EntityModel<String> {

    private String application;

    private String role;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
