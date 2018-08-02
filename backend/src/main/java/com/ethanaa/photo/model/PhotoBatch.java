package com.ethanaa.photo.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PHOTO_BATCH",
        indexes = { @Index(name = "username_idx",  columnList="username", unique = true) })
@EntityListeners(AuditingEntityListener.class)
public class PhotoBatch implements Serializable {

    @Id
    private String id;

    @Column
    private String username;

    @Transient
    private MultipartFile[] photoFiles;

    @OneToMany(
            mappedBy = "photoBatch",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Photo> photos = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public PhotoBatch(Authentication authentication,
                      String id,
                      MultipartFile[] photoFiles,
                      String outputDirectory) {

        this.createdAt = LocalDateTime.now();

        this.username = authentication.getName();
        this.id = id;
        this.photoFiles = photoFiles;

        for (MultipartFile photoFile : photoFiles) {
            this.photos.add(new Photo(outputDirectory, this.username, id, photoFile, this.createdAt));
        }
    }

    public PhotoBatch() {
        //
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MultipartFile[] getPhotoFiles() {
        return photoFiles;
    }

    public void setPhotoFiles(MultipartFile[] photoFiles) {
        this.photoFiles = photoFiles;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
        photo.setPhotoBatch(this);
    }

    public void removePhoto(Photo photo) {
        this.photos.remove(photo);
        photo.setPhotoBatch(null);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoBatch that = (PhotoBatch) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, username);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("username", username)
                .add("photos", photos)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .toString();
    }
}
