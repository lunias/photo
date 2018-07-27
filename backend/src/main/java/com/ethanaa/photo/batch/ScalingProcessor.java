package com.ethanaa.photo.batch;

import com.ethanaa.photo.model.PhotoData;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
@StepScope
public class ScalingProcessor implements ItemProcessor<File, PhotoData> {

    private static final Logger LOG = LoggerFactory.getLogger(ScalingProcessor.class);

    private static final int MAX_WIDTH = 1_000;

    private static BufferedImage WATERMARK;

    static {
        try {
            WATERMARK = ImageIO.read(new ClassPathResource("static/watermark.png").getInputStream());
        } catch (IOException ioe) {
            LOG.error("Failed to load watermark!", ioe);
        }
    }

    private String batchId;

    public ScalingProcessor(@Value("#{jobParameters['batchId']}") String batchId) {

        this.batchId = batchId;
    }

    @Override
    public PhotoData process(File file) throws Exception {

        BufferedImage photo = ImageIO.read(file);
        if (photo.getWidth() > MAX_WIDTH) {
            photo = Scalr.resize(ImageIO.read(file), Scalr.Mode.AUTOMATIC, MAX_WIDTH);
        }

        photo = Thumbnails.of(photo)
                .size(photo.getWidth(), photo.getHeight())
                .watermark(Positions.CENTER, WATERMARK, 0.8f)
                .asBufferedImage();

        LOG.info("Created scaled for " + file.getCanonicalPath());

        return new PhotoData(batchId, file, photo, PhotoData.Type.SCALED);
    }
}
