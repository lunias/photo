package com.ethanaa.photo.batch;

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
public class ThumbnailProcessor implements ItemProcessor<File, PhotoData> {

    private static final Logger LOG = LoggerFactory.getLogger(ThumbnailProcessor.class);

    private String batchId;

    public ThumbnailProcessor(@Value("#{jobParameters['batchId']}") String batchId) {

        this.batchId = batchId;
    }

    @Override
    public PhotoData process(File file) throws Exception {

        BufferedImage thumbnail = Thumbnails.of(file)
                .size(160, 160)
                .asBufferedImage();

        LOG.info("Created thumbnail for " + file.getCanonicalPath());

        return new PhotoData(batchId, file, thumbnail, PhotoData.Type.THUMBNAIL);
    }
}
