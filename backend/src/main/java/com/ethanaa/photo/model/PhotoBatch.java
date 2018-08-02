package com.ethanaa.photo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "PHOTO_BATCH",
        indexes = { @Index(name = "username_idx",  columnList="username", unique = false) })
@EntityListeners(AuditingEntityListener.class)
public class PhotoBatch implements Iterable<Photo>, Serializable {

    @EmbeddedId
    private PhotoBatchId id;

    @JsonBackReference
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
                      String outputDirectory) throws IOException {

        this.createdAt = LocalDateTime.now();

        this.id = new PhotoBatchId(id, authentication.getName());

        for (MultipartFile photoFile : photoFiles) {
            addPhoto(new Photo(outputDirectory, authentication.getName(), id, photoFile, this.createdAt));
        }
    }

    public PhotoBatch() {
        //
    }

    public PhotoBatchId getId() {
        return id;
    }

    public void setId(PhotoBatchId id) {
        this.id = id;
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
        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("photos", photos)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .toString();
    }

    @Override
    public Iterator<Photo> iterator() {

        return this.getPhotos().iterator();
    }
}
