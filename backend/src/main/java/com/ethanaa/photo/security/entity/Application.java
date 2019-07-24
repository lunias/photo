package com.ethanaa.photo.security.entity;


import com.ethanaa.photo.security.entity.base.UuidEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "APPLICATION")
public class Application extends UuidEntity {

    @Column(name = "name", length = 50, unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    private String name;

    @Column(name = "enabled")
    @NotNull
    private Boolean enabled = false;

    @OneToMany(mappedBy = "application")
    private List<ApplicationRole> applicationRoles = new ArrayList<>();

    public Application() {
        //
    }

    public Application(String name, List<ApplicationRole> applicationRoles, boolean enabled) {

        this.name = name;
        this.applicationRoles = applicationRoles;
        this.enabled = enabled;
    }

    public Application(String name, List<ApplicationRole> applicationRoles) {

        this(name, applicationRoles, true);
    }

    public Application(String name, boolean enabled) {

        this(name, new ArrayList<>(), enabled);
    }

    public Application(String name) {

        this(name, true);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<ApplicationRole> getApplicationRoles() {
        return applicationRoles;
    }

    public void setApplicationRoles(List<ApplicationRole> applicationRoles) {
        this.applicationRoles = applicationRoles;
    }
}
