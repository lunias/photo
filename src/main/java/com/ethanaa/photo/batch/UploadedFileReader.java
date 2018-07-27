package com.ethanaa.photo.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
@StepScope
public class UploadedFileReader implements ItemReader<File> {

    private static final Logger LOG = LoggerFactory.getLogger(UploadedFileReader.class);

    private ConcurrentLinkedDeque<File> uploadedFiles;

    public UploadedFileReader(@Value("#{jobParameters['batchDirectory']}") String batchDirectory) {

        this.uploadedFiles = new ConcurrentLinkedDeque<>(Arrays.asList(new File(batchDirectory).listFiles()));
    }

    @Override
    public File read() throws Exception {

        File file = uploadedFiles.pollFirst();

        if (file != null) {
            LOG.info("Read file from {}", file);
        }

        return file;
    }
}
