package com.ethanaa.photo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PhotoService {

    private static final Logger LOG = LoggerFactory.getLogger(PhotoService.class);

    private static final String OUTPUT_DIR = "/home/lunias/Pictures/photo/";
    private static final String UPLOAD_DIR = OUTPUT_DIR + "uploads/";
    private static final String THUMBS_DIR = OUTPUT_DIR + "thumbs/";
    private static final String SCALED_DIR = OUTPUT_DIR + "scaled/";

    private ConcurrentHashMap<String, CompletableFuture<ResponseEntity<?>>> uploadJobs;

    private JobLauncher jobLauncher;
    private Job uploadJob;

    @Autowired
    public PhotoService(JobLauncher jobLauncher,
                        Job uploadJob) {

        this.uploadJobs = new ConcurrentHashMap<>();

        this.jobLauncher = jobLauncher;
        this.uploadJob = uploadJob;
    }

    public File createUploadDirectory(UUID batchId) {

        String uploadDirectoryPath = UPLOAD_DIR + batchId + "/";

        File uploadDirectory = new File(uploadDirectoryPath);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        return uploadDirectory;
    }

    @Async
    public void transferToDirectory(MultipartFile photoFile, File directory, int index,
                                    CompletableFuture<Integer> transferFuture) throws IOException {

        String originalName = StringUtils.cleanPath(photoFile.getOriginalFilename());

        if (photoFile.isEmpty()) {
            LOG.error("Cannot transfer empty file {}", originalName);
        }
        if (originalName.contains("..")) {
            LOG.error("Cannot transfer file with relative path outside current directory {}", originalName);
        }

        File uploadDirectory = new File(directory + "/" + originalName);
        if (originalName.contains("/") && !uploadDirectory.getParentFile().exists()) {
            uploadDirectory.getParentFile().mkdirs();
        }

        photoFile.transferTo(uploadDirectory);

        transferFuture.complete(index);
    }

    public CompletableFuture<ResponseEntity<?>> runUploadJob(UUID batchId)
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {

        CompletableFuture<ResponseEntity<?>> completableFuture =
                uploadJobs.computeIfAbsent(batchId.toString(), (k) -> new CompletableFuture<>());

        jobLauncher.run(uploadJob, new JobParameters(new HashMap<String, JobParameter>() {
            {
                put("batchId", new JobParameter(batchId.toString(), true));
                put("batchDirectory", new JobParameter(UPLOAD_DIR + batchId, false));
                put("thumbnailDirectory", new JobParameter(THUMBS_DIR + batchId, false));
                put("scaledDirectory", new JobParameter(SCALED_DIR + batchId, false));
            }
        }));

        return completableFuture;
    }

    public void finalizeUpload(String batchId, JobExecution jobExecution) {

        CompletableFuture<ResponseEntity<?>> uploadFuture = uploadJobs.get(batchId);
        if (uploadFuture == null) {
            return;
        }

        ResponseEntity<Void> responseEntity = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        if (BatchStatus.COMPLETED.equals(jobExecution.getStatus())) {
            responseEntity = ResponseEntity.ok().build();
        } else {
            LOG.error("JobExecution not COMPLETED; {}", jobExecution.getStatus());
        }

        uploadFuture.complete(responseEntity);
        uploadJobs.remove(batchId);

        LOG.info("Upload finalized: {}", jobExecution.getStatus());
    }
}
