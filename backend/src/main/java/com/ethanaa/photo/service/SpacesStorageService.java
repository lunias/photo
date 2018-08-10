package com.ethanaa.photo.service;

import com.ethanaa.photo.config.Profiles;
import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.model.PhotoType;
import com.ethanaa.photo.model.exception.PhotoDeleteException;
import com.ethanaa.photo.model.exception.PhotoWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(Profiles.DIGITAL_OCEAN)
public class SpacesStorageService implements PhotoStorageService {

    private static final Logger LOG = LoggerFactory.getLogger(SpacesStorageService.class);

    @Override
    public void write(Photo photo, PhotoType photoType) throws PhotoWriteException {

        LOG.warn("Digital Ocean NOOP");
    }

    @Override
    public void delete(Photo photo, PhotoType photoType) throws PhotoDeleteException {

        LOG.warn("Digital Ocean NOOP");
    }

    @Override
    public void delete(Photo photo) throws PhotoDeleteException {

        LOG.warn("Digital Ocean NOOP");
    }

    @Override
    public void deleteAll() throws PhotoDeleteException {

        LOG.warn("Digital Ocean NOOP");
    }
}
