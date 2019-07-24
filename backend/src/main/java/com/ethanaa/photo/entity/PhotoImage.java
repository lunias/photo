package com.ethanaa.photo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.awt.image.BufferedImage;
import java.io.Serializable;

@Embeddable
public class PhotoImage implements Serializable {

    @JsonIgnore
    @Transient
    private BufferedImage image;

    @Column(nullable = false)
    private String path;

    public PhotoImage(BufferedImage image, String path) {

        this.image = image;
        this.path = path;
    }

    public PhotoImage(BufferedImage image) {

        this.image = image;
    }

    public PhotoImage() {
        //
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("path", path)
                .toString();
    }
}
