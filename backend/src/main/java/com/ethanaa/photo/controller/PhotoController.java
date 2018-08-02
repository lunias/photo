package com.ethanaa.photo.controller;

import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.repository.PhotoRepository;
import com.ethanaa.photo.service.PhotoBatchService;
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

import java.io.IOException;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    private static final Logger LOG = LoggerFactory.getLogger(PhotoController.class);

    private PhotoBatchService photoBatchService;
    private PhotoRepository photoRepository;

    @Autowired
    public PhotoController(PhotoBatchService photoBatchService,
                           PhotoRepository photoRepository) {

        this.photoBatchService = photoBatchService;
        this.photoRepository = photoRepository;
    }

    @GetMapping
    public ResponseEntity<Page<Photo>> getAllPhotos(Pageable pageable) {

        return ResponseEntity.ok(photoRepository.findAll(pageable));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllPhotos() {

        photoRepository.deleteAll();

        // TODO clean up disk

        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(Authentication authentication,
                                         @RequestParam("batchId") String batchId,
                                         @RequestParam("file") MultipartFile[] photoFiles) throws IOException {

        LOG.info("{} uploading photo for batch {} (size: {})", authentication.getName(), batchId, photoFiles.length);

        photoBatchService.submit(authentication, batchId, photoFiles);

        return ResponseEntity.ok(batchId);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {

        return ResponseEntity.ok("Hello there from backend!");
    }
}
