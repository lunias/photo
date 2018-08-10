package com.ethanaa.photo.service;

import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.model.PhotoType;
import com.ethanaa.photo.model.exception.PhotoDeleteException;
import com.ethanaa.photo.model.exception.PhotoWriteException;

public interface PhotoStorageService {

    void write(Photo photo, PhotoType photoType) throws PhotoWriteException;

    void delete(Photo photo, PhotoType photoType) throws PhotoDeleteException;
    void delete(Photo photo) throws PhotoDeleteException;

    void deleteAll() throws PhotoDeleteException;
}
