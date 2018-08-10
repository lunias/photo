package com.ethanaa.photo.model.exception;

import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.model.PhotoType;

import java.io.IOException;

public class PhotoWriteException extends IOException {

    public PhotoWriteException(Photo photo, PhotoType photoType, String message) {

        super("Failed to write photo (" + message + "): " + photo.getPath(photoType));
    }

    public PhotoWriteException(Photo photo, PhotoType photoType, Throwable throwable) {

        super("Failed to write photo (" + throwable.getMessage() + "): " + photo.getPath(photoType), throwable);
    }

    public PhotoWriteException(Photo photo, PhotoType photoType) {

        this(photo, photoType, "");
    }
}
