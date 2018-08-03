package com.ethanaa.photo.service;

import com.ethanaa.photo.config.PhotoProperties;
import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.model.PhotoBatch;
import com.ethanaa.photo.repository.PhotoBatchRepository;
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

    private String outputDirectory;

    private PhotoBatchRepository photoBatchRepository;

    private JobLauncher jobLauncher;
    private Job uploadJob;

    @Autowired
    public PhotoBatchService(PhotoProperties photoProperties,
                             PhotoBatchRepository photoBatchRepository,
                             JobLauncher jobLauncher,
                             Job uploadJob) {

        this.outputDirectory = photoProperties.getOutputDir();
        this.photoBatchRepository = photoBatchRepository;

        this.jobLauncher = jobLauncher;
        this.uploadJob = uploadJob;
    }

    public void submit(Authentication authentication, String batchId, MultipartFile[] photoFiles) throws IOException {

        PhotoBatch photoBatch = new PhotoBatch(authentication, batchId, photoFiles, this.outputDirectory);

        photoBatches.computeIfAbsent(batchId, (k) -> {

            photoBatchRepository.save(photoBatch);

            try {
                runUploadJob(batchId, authentication.getName());
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
        }

        return photo;
    }

    private void runUploadJob(String batchId, String username)
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {

        jobLauncher.run(uploadJob, new JobParameters(new HashMap<String, JobParameter>() {
            {
                put("batchId", new JobParameter(batchId, true));
                put("username", new JobParameter(username, true));
                put("ranAt", new JobParameter(System.currentTimeMillis(), true));
            }
        }));
    }

}
