package com.ethanaa.photo.batch;

import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.service.PhotoBatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class UploadedPhotoReader implements ItemReader<Photo> {

    private static final Logger LOG = LoggerFactory.getLogger(UploadedPhotoReader.class);

    private PhotoBatchService photoBatchService;

    private String batchId;

    public UploadedPhotoReader(PhotoBatchService photoBatchService,
                               @Value("#{jobParameters['batchId']}") String batchId) {

        this.photoBatchService = photoBatchService;
        this.batchId = batchId;
    }

    @Override
    public Photo read() throws Exception {

        Photo photo = photoBatchService.getNextPhotoInBatch(batchId);

        if (photo != null) {
            LOG.info("Read photo: {}", photo);
        }

        return photo;
    }
}
