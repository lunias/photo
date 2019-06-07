package com.ethanaa.photo.batch;

import com.ethanaa.photo.model.Photo;
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
public class DatabaseWriter implements ItemWriter<Photo> {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseWriter.class);

    private PhotoService photoService;

    @Autowired
    public DatabaseWriter(PhotoService photoService) {

        this.photoService = photoService;
    }

    @Override
    public void write(List<? extends Photo> photos) {

        photoService.saveAll(photos);
    }
}
