package com.ethanaa.photo.batch;

import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.model.PhotoBatch;
import com.ethanaa.photo.model.PhotoData;
import com.ethanaa.photo.repository.PhotoBatchRepository;
import com.ethanaa.photo.repository.PhotoDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.List;

@Component
@StepScope
public class ThumbnailFileWriter implements ItemWriter<PhotoBatch> {

    private static final Logger LOG = LoggerFactory.getLogger(ThumbnailFileWriter.class);

    private PhotoBatchRepository photoBatchRepository;

    @Autowired
    public ThumbnailFileWriter(PhotoBatchRepository photoBatchRepository) {

        this.photoBatchRepository = photoBatchRepository;
    }

    @Override
    public void write(List<? extends PhotoBatch> photoBatches) throws Exception {

        for (PhotoBatch photoBatch : photoBatches) {

            for (Photo photo : photoBatch.getPhotos()) {

                photo.writeToThumb();

                LOG.info("Wrote thumbnail for {}", photo.getOriginalFilename());
            }
        }

        photoBatchRepository.saveAll(photoBatches);
    }
}
