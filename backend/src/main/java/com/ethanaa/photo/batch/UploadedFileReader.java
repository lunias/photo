package com.ethanaa.photo.batch;

import com.ethanaa.photo.model.PhotoBatch;
import com.ethanaa.photo.service.PhotoBatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@StepScope
public class UploadedFileReader implements ItemReader<PhotoBatch> {

    private static final Logger LOG = LoggerFactory.getLogger(UploadedFileReader.class);

    private PhotoBatchService photoBatchService;

    public UploadedFileReader(PhotoBatchService photoBatchService) {

        this.photoBatchService = photoBatchService;
    }

    @Override
    public PhotoBatch read() throws Exception {

        PhotoBatch photoBatch = photoBatchService.poll();

        if (photoBatch != null) {
            LOG.info("Read photo batch: {}", photoBatch);
        }

        return photoBatch;
    }
}
