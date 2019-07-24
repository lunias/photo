package com.ethanaa.photo.security.entity;

import com.ethanaa.photo.security.entity.base.UuidEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "USER")
public class User extends UuidEntity {

    @Column(name = "username", length = 50, unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    private String username;

    @Column(name = "password", length = 100)
    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    @Column(name = "firstname", length = 50)
    @NotNull
    @Size(min = 3, max = 50)
    private String firstname;

    @Column(name = "lastname", length = 50)
    @NotNull
    @Size(min = 3, max = 50)
    private String lastname;

    @Column(name = "email", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String email;

    @Column(name = "enabled")
    @NotNull
    private Boolean enabled = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="USER_APPLICATION_ROLE",
            joinColumns=@JoinColumn(name="user_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="application_role_id", referencedColumnName="id"))
    private List<ApplicationRole> applicationRoles = new ArrayList<>();

    public User() {
        //
    }

    public User(String username, String password, String firstname, String lastname, String email, Boolean enabled) {

        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.enabled = enabled;
    }

    public User(String username, String password, String firstname, String lastname, String email) {

        this(username, password, firstname, lastname, email, true);
    }

    @JsonIgnore
    public Map<String, List<Role>> getRolesForApplications(List<String> applications) {

        Map<String, List<Role>> roles = new HashMap<>();

        List<ApplicationRole> filteredApplicationRoles = this.applicationRoles.stream()
                .filter(ar -> applications.contains(ar.getApplication().getName()))
                .collect(Collectors.toList());

        for (ApplicationRole filteredApplicationRole : filteredApplicationRoles) {
            roles.computeIfAbsent(filteredApplicationRole.getApplication().getName(),
                    name -> new ArrayList<>())
                    .add(filteredApplicationRole.getRole());
        }

        return roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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