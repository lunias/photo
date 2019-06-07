package com.ethanaa.photo.service;

import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.model.PhotoType;
import com.ethanaa.photo.model.exception.PhotoWriteException;
import com.ethanaa.photo.repository.PhotoRepository;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.BiFunction;

@Service
@Transactional
public class PhotoService {

    private static final Logger LOG = LoggerFactory.getLogger(PhotoService.class);

    private PhotoStorageService photoStorageService;
    private PhotoRepository photoRepository;

    private BiFunction<Photo, PhotoType, String> writePathResolver;

    @Autowired
    public PhotoService(PhotoStorageService photoStorageService,
                        PhotoRepository photoRepository,
                        BiFunction<Photo, PhotoType, String> writePathResolver) {

        this.photoStorageService = photoStorageService;
        this.photoRepository = photoRepository;
        this.writePathResolver = writePathResolver;
    }

    public Page<Photo> getPhotos(Pageable pageable) {

        return photoRepository.findAll(pageable);
    }

    public Photo write(Photo photo, PhotoType photoType) throws PhotoWriteException {

        photo.setPath(photoType, writePathResolver.apply(photo, photoType));

        photoStorageService.write(photo, photoType);

        LOG.info("Wrote {} for photo: {}", photoType, photo);

        return photo;
    }

    public <T extends Photo> Iterable<T> saveAll(Iterable<T> photos) {

        Iterable<T> savedPhotos = photoRepository.saveAll(photos);

        LOG.info("Wrote [{}] photo(s) to the database: \n\t{}",
                Iterables.size(savedPhotos),
                Joiner.on("\n\t").join(savedPhotos));

        return savedPhotos;
    }

    public void delete(Photo photo) {

        photoStorageService.delete(photo);
        photoRepository.delete(photo);

        LOG.info("Deleted photo: {}", photo);
    }

    public void deleteAll() {

        photoStorageService.deleteAll();
        photoRepository.deleteAll();

        LOG.info("Deleted all photos");
    }
}
