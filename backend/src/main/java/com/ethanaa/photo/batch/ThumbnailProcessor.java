package com.ethanaa.photo.batch;

import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.model.PhotoBatch;
import com.ethanaa.photo.model.PhotoData;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.File;

@Component
@StepScope
public class ThumbnailProcessor implements ItemProcessor<PhotoBatch, PhotoBatch> {

    private static final Logger LOG = LoggerFactory.getLogger(ThumbnailProcessor.class);

    public ThumbnailProcessor() {

    }

    @Override
    public PhotoBatch process(PhotoBatch photoBatch) throws Exception {

        for (Photo photo : photoBatch.getPhotos()) {

            BufferedImage thumbnail = Thumbnails.of(photo.getPhotoFile().getInputStream())
                    .size(160, 160)
                    .asBufferedImage();

            photo.setPhotoImage(thumbnail);

            LOG.info("Created thumbnail for " + photo.getOriginalFilename());
        }

        return photoBatch;
    }
}
