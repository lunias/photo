package com.ethanaa.photo.batch;

import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.repository.PhotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class ScaledPhotoWriter implements ItemWriter<Photo> {

    private static final Logger LOG = LoggerFactory.getLogger(ScaledPhotoWriter.class);

    private PhotoRepository photoRepository;

    @Autowired
    public ScaledPhotoWriter(PhotoRepository photoRepository) {

        this.photoRepository = photoRepository;
    }

    @Override
    public void write(List<? extends Photo> photos) throws Exception {

        for (Photo photo : photos) {

            photo.writeToScaled();
            photo.setScaledImage(null);

            photoRepository.save(photo);

            LOG.info("Wrote scaled for {}", photo);
        }
    }
}
