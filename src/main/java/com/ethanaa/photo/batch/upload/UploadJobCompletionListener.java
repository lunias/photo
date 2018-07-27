package com.ethanaa.photo.batch.upload;

import com.ethanaa.photo.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class UploadJobCompletionListener extends JobExecutionListenerSupport {

    private static final Logger LOG = LoggerFactory.getLogger(UploadJobCompletionListener.class);

    private PhotoService photoService;

    @Autowired
    public UploadJobCompletionListener(@Lazy PhotoService photoService) {

        this.photoService = photoService;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        String batchId = jobExecution.getJobParameters().getString("batchId");

        photoService.finalizeUpload(batchId, jobExecution);
    }
}
