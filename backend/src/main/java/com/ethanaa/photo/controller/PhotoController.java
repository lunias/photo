package com.ethanaa.photo.controller;

import com.ethanaa.photo.model.PhotoBatch;
import com.ethanaa.photo.model.PhotoData;
import com.ethanaa.photo.repository.PhotoDataRepository;
import com.ethanaa.photo.service.PhotoBatchService;
import com.ethanaa.photo.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    private static final Logger LOG = LoggerFactory.getLogger(PhotoController.class);

    private PhotoService photoService;
    private PhotoBatchService photoBatchService;
    private PhotoDataRepository photoDataRepository;

    @Autowired
    public PhotoController(PhotoService photoService,
                           PhotoBatchService photoBatchService,
                           PhotoDataRepository photoDataRepository) {

        this.photoService = photoService;
        this.photoBatchService = photoBatchService;
        this.photoDataRepository = photoDataRepository;
    }

    @GetMapping
    public ResponseEntity<Page<PhotoData>> getAllPhotoData(Pageable pageable) {

        return ResponseEntity.ok(photoDataRepository.findAll(pageable));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllPhotoData() {

        photoService.deleteAllPhotoData();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {

        return ResponseEntity.ok("Hello there from backend!");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(Authentication authentication,
                                         @RequestParam("batchId") String batchId,
                                         @RequestParam("file") MultipartFile[] photoFiles)
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException, IOException, InterruptedException, ExecutionException, TimeoutException {

        String uploadId = UUID.randomUUID().toString();

        photoBatchService.submit(authentication, batchId, photoFiles);

        LOG.info("Creating upload directory");

        File uploadDirectory = photoService.createUploadDirectory(authentication, batchId, uploadId);

        LOG.info("Transferring {} file(s)...", photoFiles.length);

        List<CompletableFuture<Integer>> transfers = new ArrayList<>(photoFiles.length);

        int i = 0;
        for (MultipartFile photoFile : photoFiles) {

            CompletableFuture<Integer> transferFuture = new CompletableFuture<>();
            transfers.add(transferFuture);

            photoService.transferToDirectory(photoFile, uploadDirectory,
                    ++i, transferFuture);
        }

        for (CompletableFuture<Integer> transferFuture : transfers) {
            Integer index = transferFuture.get(60, TimeUnit.SECONDS);
            LOG.info("Transferred {} / {}", index, photoFiles.length);
        }

        photoService.runUploadJob(authentication, batchId, uploadId);

        return ResponseEntity.ok(batchId);
    }
}
