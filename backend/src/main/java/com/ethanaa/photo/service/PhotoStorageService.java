package com.ethanaa.photo.service;

import com.ethanaa.photo.entity.Photo;
import com.ethanaa.photo.entity.PhotoType;
import com.ethanaa.photo.entity.exception.PhotoDeleteException;
import com.ethanaa.photo.entity.exception.PhotoWriteException;

public interface PhotoStorageService {

    void write(Photo photo, PhotoType photoType) throws PhotoWriteException;

    void delete(Photo photo, PhotoType photoType) throws PhotoDeleteException;
    void delete(Photo photo) throws PhotoDeleteException;

    void deleteAll() throws PhotoDeleteException;
}
