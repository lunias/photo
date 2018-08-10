package com.ethanaa.photo.model.exception;

import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.model.PhotoType;

import java.io.IOException;

public class PhotoDeleteException extends SecurityException {

    public PhotoDeleteException(Photo photo, PhotoType photoType, String message) {

        super("Failed to delete photo (" + message + "): " + photo.getPath(photoType));
    }

    public PhotoDeleteException(Photo photo, PhotoType photoType, Throwable throwable) {

        super("Failed to delete photo (" + throwable.getMessage() + "): " + photo.getPath(photoType), throwable);
    }

    public PhotoDeleteException(Photo photo, Throwable throwable) {

        super("Failed to delete photo (" + throwable.getMessage() + "): " + photo, throwable);
    }

    public PhotoDeleteException(String outputDirectory, Throwable throwable) {

        super("Failed to delete photo output directory (" + throwable.getMessage() + "): " + outputDirectory, throwable);
    }

    public PhotoDeleteException(Photo photo, PhotoType photoType) {

        this(photo, photoType, "");
    }
}
