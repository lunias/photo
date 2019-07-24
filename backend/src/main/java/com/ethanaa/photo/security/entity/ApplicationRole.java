package com.ethanaa.photo.security.entity;


import com.ethanaa.photo.security.entity.base.UuidEntity;

import javax.persistence.*;

@Entity
@Table(name = "APPLICATION_ROLE")
public class ApplicationRole extends UuidEntity {

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="application_id")
    private Application application;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="role_id")
    private Role role;

    public ApplicationRole() {
        //
    }

    public ApplicationRole(Application application, Role role) {

        this.application = application;
        this.role = role;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
