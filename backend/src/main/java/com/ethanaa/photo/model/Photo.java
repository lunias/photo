package com.ethanaa.photo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private MultipartFile photoFile;

    @JsonIgnore
    @Transient
    private BufferedImage photoImage;

    @Column(name = "original_filename")
    private String originalFilename;
    @Column
    private String extension;
    @Column(name = "content_type")
    private String contentType;
    @Column
    private long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private PhotoBatch photoBatch;

    public Photo(String outputDirectory, String username, String batchId, MultipartFile photoFile, LocalDateTime createdAt) {

        if (photoFile.isEmpty()) {
            throw new RuntimeException("Empty file");
        }

        this.photoFile = photoFile;

        this.originalFilename = photoFile.getOriginalFilename();

        if (this.originalFilename == null || this.originalFilename.contains("..")) {
            throw new RuntimeException("Invalid filename");
        }

        this.extension = Files.getFileExtension(this.originalFilename);
        this.contentType = photoFile.getContentType();
        this.size = photoFile.getSize();

        this.rawPath = createPath(outputDirectory, username, createdAt.toLocalDate(), PhotoData.Type.RAW, batchId);
        this.thumbPath = createPath(outputDirectory, username, createdAt.toLocalDate(), PhotoData.Type.THUMBNAIL, batchId);
        this.scaledPath = createPath(outputDirectory, username, createdAt.toLocalDate(), PhotoData.Type.SCALED, batchId);

        this.rawPath += this.originalFilename;
        this.thumbPath += this.originalFilename;
        this.scaledPath += this.originalFilename;
    }

    public Photo() {
        //
    }

    private static String createPath(String outputDir, String username, LocalDate createDate, PhotoData.Type type, String batchId) {

        return outputDir + username +
                "/" + createDate +
                "/" + batchId +
                "/" + type +
                "/";
    }

    public void writeToRaw() throws IOException {

        if (this.photoFile == null) {
            throw new RuntimeException("Null photo file");
        }

        this.photoFile.transferTo(getFile(PhotoData.Type.RAW));
    }

    public void writeToScaled() throws IOException {

        if (this.photoImage == null) {
            throw new RuntimeException("Null photo image");
        }

        ImageIO.write(this.photoImage, this.extension, getFile(PhotoData.Type.SCALED));
    }

    public void writeToThumb() throws IOException {

        if (this.photoImage == null) {
            throw new RuntimeException("Null photo image");
        }

        ImageIO.write(this.photoImage, this.extension, getFile(PhotoData.Type.THUMBNAIL));
    }

    public void deleteFile(PhotoData.Type type) {

        File photoFile = new File(getPath(type));
        photoFile.delete();
    }

    private File getFile(PhotoData.Type type) {

        File file = new File(getPath(type));

        if (this.originalFilename.contains("/") && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        return file;
    }

    private String getPath(PhotoData.Type type) {

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

    public MultipartFile getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(MultipartFile photoFile) {
        this.photoFile = photoFile;
    }

    public BufferedImage getPhotoImage() {
        return photoImage;
    }

    public void setPhotoImage(BufferedImage photoImage) {
        this.photoImage = photoImage;
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
