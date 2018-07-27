package com.ethanaa.photo.controller;

import com.ethanaa.photo.model.PhotoData;
import com.ethanaa.photo.repository.PhotoDataRepository;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    private PhotoDataRepository photoDataRepository;

    @Autowired
    public PhotoController(PhotoService photoService,
                           PhotoDataRepository photoDataRepository) {

        this.photoService = photoService;
        this.photoDataRepository = photoDataRepository;
    }

    @GetMapping
    public ResponseEntity<Page<PhotoData>> getAllPhotoData(Pageable pageable) {

        return ResponseEntity.ok(photoDataRepository.findAll(pageable));
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {

        return ResponseEntity.ok("Hello there from backend!");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("photos") MultipartFile[] photoFiles)
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException, IOException, InterruptedException, ExecutionException, TimeoutException {

        UUID batchId = UUID.randomUUID();

        LOG.info("Creating upload directory");

        File uploadDirectory = photoService.createUploadDirectory(batchId);

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

        photoService.runUploadJob(batchId);

        return ResponseEntity.ok(batchId.toString());
    }
}
