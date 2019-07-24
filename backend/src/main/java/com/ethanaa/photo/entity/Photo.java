package com.ethanaa.photo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "PHOTO")
@EntityListeners(AuditingEntityListener.class)
public class Photo implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Photo.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "photo_type", length = 32)
    @Column(name = "photo_image")
    @CollectionTable(name = "PHOTO_IMAGE",
            joinColumns = @JoinColumn(name = "photo_id", referencedColumnName = "id"))
    private Map<PhotoType, PhotoImage> photoImages = new HashMap<>();

    @Lob
    @Column(name = "thumb_src", columnDefinition = "CLOB")
    private String thumbSrc;

    @Column(name = "original_filename")
    private String originalFilename;

    @Column
    private String extension;

    @Column(name = "content_type")
    private String contentType;

    @Column
    private long size;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(
                    name = "batch_id",
                    referencedColumnName = "id"),
            @JoinColumn(
                    name = "username",
                    referencedColumnName = "username")
    })
    private PhotoBatch photoBatch;

    public Photo(MultipartFile photoFile) throws IOException {

        if (photoFile.isEmpty()) {
            throw new RuntimeException("Empty file");
        }

        this.originalFilename = photoFile.getOriginalFilename();

        if (this.originalFilename == null || this.originalFilename.contains("..")) {
            throw new RuntimeException("Invalid filename");
        }

        this.extension = Files.getFileExtension(this.originalFilename);
        this.contentType = photoFile.getContentType();
        this.size = photoFile.getSize();

        setPhotoImage(PhotoType.RAW, new PhotoImage(ImageIO.read(photoFile.getInputStream())));
    }

    public Photo() {
        //
    }

    @JsonIgnore
    public List<String> getPaths() {

        return photoImages.values().stream()
                .map(PhotoImage::getPath)
                .collect(Collectors.toList());
    }

    public void setPhotoImage(PhotoType type, PhotoImage photoImage) {

        photoImages.put(type, photoImage);
    }

    @JsonIgnore
    public BufferedImage getImage(PhotoType type) {

        PhotoImage photoImage = photoImages.get(type);
        if (photoImage != null) {
            return photoImage.getImage();
        }

        return null;
    }

    @JsonIgnore
    public String getPath(PhotoType type) {

        PhotoImage photoImage = photoImages.get(type);
        if (photoImage != null) {
            return photoImage.getPath();
        }

        return null;
    }

    public void setPath(PhotoType type, String path) {

        PhotoImage photoImage = photoImages.get(type);
        if (photoImage != null) {
            photoImage.setPath(path);
        }
    }

    public void clearImage(PhotoType type) {

        PhotoImage photoImage = photoImages.get(type);
        if (photoImage != null) {
            photoImage.setImage(null);
        }
    }

    public void clearImages() {

        for (PhotoImage photoImage : photoImages.values()) {
            photoImage.setImage(null);
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Map<PhotoType, PhotoImage> getPhotoImages() {
        return photoImages;
    }

    public void setPhotoImages(Map<PhotoType, PhotoImage> photoImages) {
        this.photoImages = photoImages;
    }

    public String getThumbSrc() {
        return thumbSrc;
    }

    public void setThumbSrc(BufferedImage thumbImage) {

        if (thumbImage == null) {
            return;
        }

        try (ByteArrayOutputStream boas = new ByteArrayOutputStream()) {

            ImageIO.write(thumbImage, getExtension(), boas);
            boas.flush();

            this.thumbSrc = "data:" + this.getContentType() + ";charset=utf-8;base64,"
                    + Base64.getEncoder().encodeToString(boas.toByteArray());

        } catch (IOException ioe) {
            LOG.error("Failed to set thumbSrc", ioe);
        }
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public PhotoBatch getPhotoBatch() {
        return photoBatch;
    }

    public void setPhotoBatch(PhotoBatch photoBatch) {
        this.photoBatch = photoBatch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equal(id, photo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("originalFilename", originalFilename)
                .add("contentType", contentType)
                .add("size", size)
                .toString();
    }
}
