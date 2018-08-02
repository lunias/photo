package com.ethanaa.photo.service;

import com.ethanaa.photo.config.PhotoProperties;
import com.ethanaa.photo.model.PhotoBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class PhotoBatchService {

    private ConcurrentLinkedQueue<PhotoBatch> photoBatches = new ConcurrentLinkedQueue<>();

    private String outputDirectory;

    @Autowired
    public PhotoBatchService(PhotoProperties photoProperties) {

        this.outputDirectory = photoProperties.getOutputDir();
    }

    public void submit(Authentication authentication, String batchId, MultipartFile[] photoFiles) {

        this.photoBatches.add(new PhotoBatch(authentication, batchId, photoFiles, this.outputDirectory));
    }

    public PhotoBatch poll() {

        return this.photoBatches.poll();
    }

}
