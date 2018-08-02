package com.ethanaa.photo.model;

import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PhotoBatchId implements Serializable {

    @Column
    private String id;

    @Column
    private String username;

    public PhotoBatchId() {
        //
    }

    public PhotoBatchId(String id, String username) {

        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoBatchId that = (PhotoBatchId) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, username);
    }
}
