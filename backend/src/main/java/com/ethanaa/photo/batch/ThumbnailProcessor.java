package com.ethanaa.photo.batch;

import com.ethanaa.photo.entity.Photo;
import com.ethanaa.photo.entity.PhotoImage;
import com.ethanaa.photo.entity.PhotoType;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
@StepScope
public class ThumbnailProcessor implements ItemProcessor<Photo, Photo> {

    private static final Logger LOG = LoggerFactory.getLogger(ThumbnailProcessor.class);

    public ThumbnailProcessor() {

    }

    @Override
    public Photo process(Photo photo) throws Exception {

        BufferedImage thumbnail = Thumbnails.of(photo.getImage(PhotoType.RAW))
                .size(160, 160)
                .asBufferedImage();

        photo.setPhotoImage(PhotoType.THUMBNAIL, new PhotoImage(thumbnail));
        photo.setThumbSrc(thumbnail);

        LOG.info("Created {} for {}", PhotoType.THUMBNAIL, photo);

        return photo;
    }
}
