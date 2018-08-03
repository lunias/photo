package com.ethanaa.photo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.io.Files;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "PHOTO")
@EntityListeners(AuditingEntityListener.class)
public class Photo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "raw_path")
    private String rawPath;
    @Column(name = "thumb_path")
    private String thumbPath;
    @Column(name = "scaled_path")
    private String scaledPath;

    @JsonIgnore
    @Transient
    private BufferedImage rawImage;

    @JsonIgnore
    @Transient
    private BufferedImage thumbImage;

    @JsonIgnore
    @Transient
    private BufferedImage scaledImage;

    @Column(name = "original_filename")
    private String originalFilename;
    @Column
    private String extension;
    @Column(name = "content_type")
    private String contentType;
    @Column
    private long size;

    @JsonIgnore
    @JsonManagedReference
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

    public Photo(String outputDirectory, String username, String batchId, MultipartFile photoFile, LocalDateTime createdAt) throws IOException {

        if (photoFile.isEmpty()) {
            throw new RuntimeException("Empty file");
        }

        this.originalFilename = photoFile.getOriginalFilename();

        if (this.originalFilename == null || this.originalFilename.contains("..")) {
            throw new RuntimeException("Invalid filename");
        }

        this.rawImage = ImageIO.read(photoFile.getInputStream());

        this.extension = Files.getFileExtension(this.originalFilename);
        this.contentType = photoFile.getContentType();
        this.size = photoFile.getSize();

        this.rawPath = createPath(outputDirectory, username, createdAt.toLocalDate(), PhotoType.RAW, batchId);
        this.thumbPath = createPath(outputDirectory, username, createdAt.toLocalDate(), PhotoType.THUMBNAIL, batchId);
        this.scaledPath = createPath(outputDirectory, username, createdAt.toLocalDate(), PhotoType.SCALED, batchId);

        this.rawPath += this.originalFilename;
        this.thumbPath += this.originalFilename;
        this.scaledPath += this.originalFilename;
    }

    public Photo() {
        //
    }

    private static String createPath(String outputDir, String username, LocalDate createDate, PhotoType type, String batchId) {

        return outputDir + username +
                "/" + createDate +
                "/" + batchId +
                "/" + type +
                "/";
    }

    public void writeToRaw() throws IOException {

        if (this.rawImage == null) {
            throw new RuntimeException("Null raw image");
        }

        ImageIO.write(this.rawImage, this.extension, getFile(PhotoType.RAW));
    }

    public void writeToScaled() throws IOException {

        if (this.scaledImage == null) {
            throw new RuntimeException("Null scaled image");
        }

        ImageIO.write(this.scaledImage, this.extension, getFile(PhotoType.SCALED));
    }

    public void writeToThumb() throws IOException {

        if (this.thumbImage == null) {
            throw new RuntimeException("Null thumb image");
        }

        ImageIO.write(this.thumbImage, this.extension, getFile(PhotoType.THUMBNAIL));
    }

    public void deleteFile(PhotoType type) {

        File photoFile = new File(getPath(type));
        photoFile.delete();
    }

    private File getFile(PhotoType type) {

        File file = new File(getPath(type));
        File parentDirectory = file.getParentFile();

        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }

        return file;
    }

    private String getPath(PhotoType type) {

        switch (type) {

            case RAW:
                return this.rawPath;
            case THUMBNAIL:
                return this.thumbPath;
            case SCALED:
                return this.scaledPath;
        }

        return "";
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

    public String getRawPath() {
        return rawPath;
    }

    public void setRawPath(String rawPath) {
        this.rawPath = rawPath;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getScaledPath() {
        return scaledPath;
    }

    public void setScaledPath(String scaledPath) {
        this.scaledPath = scaledPath;
    }

    public BufferedImage getRawImage() {
        return rawImage;
    }

    public void setRawImage(BufferedImage rawImage) {
        this.rawImage = rawImage;
    }

    public BufferedImage getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(BufferedImage thumbImage) {
        this.thumbImage = thumbImage;
    }

    public BufferedImage getScaledImage() {
        return scaledImage;
    }

    public void setScaledImage(BufferedImage scaledImage) {
        this.scaledImage = scaledImage;
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
