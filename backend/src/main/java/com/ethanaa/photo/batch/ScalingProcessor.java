package com.ethanaa.photo.batch;

import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.model.PhotoImage;
import com.ethanaa.photo.model.PhotoType;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Component
@StepScope
public class ScalingProcessor implements ItemProcessor<Photo, Photo> {

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

    public ScalingProcessor() {

    }

    @Override
    public Photo process(Photo photo) throws Exception {

        BufferedImage photoImage = photo.getImage(PhotoType.RAW);
        if (photoImage.getWidth() > MAX_WIDTH) {
            photoImage = Scalr.resize(photoImage, Scalr.Mode.AUTOMATIC, MAX_WIDTH);
        }

        photoImage = Thumbnails.of(photoImage)
                .size(photoImage.getWidth(), photoImage.getHeight())
                .watermark(Positions.CENTER, WATERMARK, 0.8f)
                .asBufferedImage();

        photo.setPhotoImage(PhotoType.SCALED, new PhotoImage(photoImage, ""));

        LOG.info("Created scaled for {}", photo);

        return photo;
    }
}
