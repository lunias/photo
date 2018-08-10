package com.ethanaa.photo.model.exception;

import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.model.PhotoType;

import java.io.IOException;

public class PhotoReadException extends IOException {

    public PhotoReadException(Photo photo, PhotoType photoType, String message) {

        super("Failed to read photo (" + message + "): " + photo.getPath(photoType));
    }

    public PhotoReadException(Photo photo, PhotoType photoType, Throwable throwable) {

        super("Failed to read photo (" + throwable.getMessage() + "): " + photo.getPath(photoType), throwable);
    }

    public PhotoReadException(Photo photo, PhotoType photoType) {

        this(photo, photoType, "");
    }
}
