package com.ethanaa.photo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.io.Files;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "PHOTO_DATA")
public class PhotoData implements Serializable {

    public static final String THUMBNAIL_SUFFIX = "thumbnail";
    public static final String SCALED_SUFFIX = "scaled";

    public enum Type {
        RAW, THUMBNAIL, SCALED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "batch_id")
    private String batchId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 16)
    private Type type;

    @Column(name = "filename")
    private String filename;

    @Column(name = "name")
    private String name;

    @Column(name = "extension")
    private String extension;

    @Column(name = "raw_path")
    private String rawPath;

    @Column(name = "derived_path")
    private String derivedPath;

    @Transient
    @JsonIgnore
    private BufferedImage bufferedImage;

    public PhotoData(String batchId, File file, BufferedImage bufferedImage, Type type) {

        this.batchId = batchId;
        this.type = type;

        this.filename = file.getName();
        this.name = Files.getNameWithoutExtension(this.filename);
        this.extension = Files.getFileExtension(this.filename);

        setRawPath(file.getPath());

        this.bufferedImage = bufferedImage;
    }

    public PhotoData(String batchId, File file, Type type) throws IOException {

        this(batchId, file, ImageIO.read(file), type);
    }

    public PhotoData() {
        //
    }

    public File createThumbnailFile(String thumbnailDirectory) {

        return createFile(thumbnailDirectory, THUMBNAIL_SUFFIX);
    }

    public File createScaledFile(String scaledDirectory) {

        return createFile(scaledDirectory, SCALED_SUFFIX);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getRawPath() {
        return rawPath;
    }

    public void setRawPath(String rawPath) {

        this.rawPath = StringUtils.cleanPath(rawPath);
    }

    public String getDerivedPath() {
        return derivedPath;
    }

    public void setDerivedPath(String derivedPath) {

        this.derivedPath = StringUtils.cleanPath(derivedPath);
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    private File createFile(String directory, String suffix) {

        if (!directory.endsWith("/")) {
            directory += "/";
        }

        File file = new File(directory
                + getName() + "." + suffix + "." + getExtension());

        setDerivedPath(file.getPath());

        setFilename(file.getName());
        setName(Files.getNameWithoutExtension(this.filename));

        return file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoData photoData = (PhotoData) o;
        return Objects.equal(derivedPath, photoData.derivedPath);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(derivedPath);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("name", name)
                .add("extension", extension)
                .toString();
    }
}
