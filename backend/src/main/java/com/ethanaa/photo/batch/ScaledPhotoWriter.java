package com.ethanaa.photo.batch;

import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.model.PhotoType;
import com.ethanaa.photo.repository.PhotoRepository;
import com.ethanaa.photo.service.PhotoService;
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

    private PhotoService photoService;

    @Autowired
    public ScaledPhotoWriter(PhotoService photoService) {

        this.photoService = photoService;
    }

    @Override
    public void write(List<? extends Photo> photos) throws Exception {

        for (Photo photo : photos) {

            photoService.write(photo, PhotoType.SCALED);
        }
    }
}
