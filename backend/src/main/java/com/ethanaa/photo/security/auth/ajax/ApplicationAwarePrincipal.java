package com.ethanaa.photo.security.auth.ajax;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.List;

public class ApplicationAwarePrincipal {

    private String username;
    private List<String> applications;

    public ApplicationAwarePrincipal() {
        //
    }

    public ApplicationAwarePrincipal(String username, List<String> applications) {

        this.username = username;
        this.applications = applications;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getApplications() {
        return applications;
    }

    public void setApplications(List<String> applications) {
        this.applications = applications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationAwarePrincipal principal = (ApplicationAwarePrincipal) o;
        return Objects.equal(username, principal.username) &&
                Objects.equal(applications, principal.applications);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username, applications);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .add("applications", applications)
                .toString();
    }
}
