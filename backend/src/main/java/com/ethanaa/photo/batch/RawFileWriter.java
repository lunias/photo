package com.ethanaa.photo.batch;

import com.ethanaa.photo.entity.Photo;
import com.ethanaa.photo.entity.PhotoType;
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
public class RawFileWriter implements ItemWriter<Photo> {

    private static final Logger LOG = LoggerFactory.getLogger(RawFileWriter.class);

    private PhotoService photoService;

    @Autowired
    public RawFileWriter(PhotoService photoService) {

        this.photoService = photoService;
    }

    @Override
    public void write(List<? extends Photo> photos) throws Exception {

        for (Photo photo : photos) {

            photoService.write(photo, PhotoType.RAW);
        }
    }
}
