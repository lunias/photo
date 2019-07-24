package com.ethanaa.photo.service;

import com.ethanaa.photo.entity.Photo;
import com.ethanaa.photo.entity.PhotoBatch;
import com.ethanaa.photo.repository.PhotoBatchRepository;
import com.ethanaa.photo.security.model.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class PhotoBatchService {

    private static final Logger LOG = LoggerFactory.getLogger(PhotoBatchService.class);

    private ConcurrentHashMap<String, LinkedBlockingQueue<Photo>> photoBatches = new ConcurrentHashMap<>();

    private static final long MAX_POLL_MS = 500L;

    private PhotoBatchRepository photoBatchRepository;

    private JobLauncher jobLauncher;
    private Job uploadJob;

    @Autowired
    public PhotoBatchService(PhotoBatchRepository photoBatchRepository,
                             JobLauncher jobLauncher,
                             Job uploadJob) {

        this.photoBatchRepository = photoBatchRepository;

        this.jobLauncher = jobLauncher;
        this.uploadJob = uploadJob;
    }

    public void submit(Authentication authentication, String batchId, MultipartFile[] photoFiles) throws IOException {

        String username = ((UserContext) authentication.getPrincipal()).getUsername();

        LOG.info("{} uploading photo for batch {} (size: {})",
                username, batchId, photoFiles.length);

        PhotoBatch photoBatch = new PhotoBatch(authentication, batchId, photoFiles);

        photoBatches.computeIfAbsent(batchId, (k) -> {

            photoBatchRepository.save(photoBatch);

            try {
                runUploadJob(batchId, username);
            } catch (JobParametersInvalidException jpie) {
                LOG.error("Invalid job parameters", jpie);
            } catch (JobExecutionAlreadyRunningException jeare) {
                LOG.error("Job is already running", jeare);
            } catch (JobRestartException jre) {
                LOG.error("Job restarted", jre);
            } catch (JobInstanceAlreadyCompleteException jiace) {
                LOG.error("Job instance already complete", jiace);
            }

            return new LinkedBlockingQueue<>();

        }).addAll(photoBatch.getPhotos());

    }

    public Photo getNextPhotoInBatch(String batchId) {

        LinkedBlockingQueue<Photo> photos = photoBatches.get(batchId);
        if (photos == null) {
            return null;
        }

        Photo photo = null;
        try {
            photo = photos.poll(MAX_POLL_MS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ie) {
            LOG.error("Waited on batch {} for {} ms. Interrupted. {}", batchId, MAX_POLL_MS, ie);
        }

        if (photo == null) {
            photoBatches.remove(batchId);
        } else {
            LOG.debug("Got photo: {}", photo);
        }

        return photo;
    }

    private void runUploadJob(String batchId, String username)
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {

        LOG.debug("Running upload job: {}/{}", username, batchId);

        jobLauncher.run(uploadJob, new JobParameters(new HashMap<>() {
            {
                put("batchId", new JobParameter(batchId, true));
                put("username", new JobParameter(username, true));
                put("ranAt", new JobParameter(System.currentTimeMillis(), true));
            }
        }));
    }

}
