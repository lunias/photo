package com.ethanaa.photo.controller;

import com.ethanaa.photo.entity.Photo;
import com.ethanaa.photo.service.PhotoBatchService;
import com.ethanaa.photo.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private PhotoService photoService;

    @Autowired
    public PhotoController(PhotoBatchService photoBatchService,
                           PhotoService photoService) {

        this.photoBatchService = photoBatchService;
        this.photoService = photoService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(Authentication authentication,
                                         @RequestParam("batchId") String batchId,
                                         @RequestParam("file") MultipartFile[] photoFiles) throws IOException {

        photoBatchService.submit(authentication, batchId, photoFiles);

        return ResponseEntity.ok(batchId);
    }

    @GetMapping
    public ResponseEntity<Page<Photo>> getAllPhotos(Pageable pageable) {

        return ResponseEntity.ok(photoService.getPhotos(pageable));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllPhotos() {

        photoService.deleteAll();

        return ResponseEntity.ok().build();
    }
}
