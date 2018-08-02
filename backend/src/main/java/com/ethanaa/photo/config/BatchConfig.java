package com.ethanaa.photo.config;

import com.ethanaa.photo.batch.*;
import com.ethanaa.photo.model.PhotoBatch;
import com.ethanaa.photo.model.PhotoData;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.io.File;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    private TaskExecutor taskExecutor;

    @Autowired
    public BatchConfig(JobBuilderFactory jobBuilderFactory,
                       StepBuilderFactory stepBuilderFactory,
                       TaskExecutor taskExecutor) {

        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;

        this.taskExecutor = taskExecutor;
    }

    @Bean
    JobLauncher jobLauncher(JobRepository jobRepository) {

        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);

        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        jobLauncher.setTaskExecutor(simpleAsyncTaskExecutor);

        return jobLauncher;
    }

    @Bean
    Job uploadJob(Step thumbnailStep, Step scaleStep) {

        Flow processFlow = new FlowBuilder<Flow>("processFlow")
                .split(taskExecutor)
                .add(
                        new FlowBuilder<Flow>("thumbnailFlow").from(thumbnailStep).end(),
                        new FlowBuilder<Flow>("scaleFlow").from(scaleStep).end()
                )
                .end();


        return jobBuilderFactory.get("uploadJob")
                .incrementer(new RunIdIncrementer())
                .start(processFlow)
                .end()
                .build();
    }

    @Bean
    Step thumbnailStep(UploadedFileReader uploadedFileReader,
                       ThumbnailProcessor thumbnailProcessor,
                       ThumbnailFileWriter thumbnailFileWriter) {

        return stepBuilderFactory.get("thumbnailStep")
                .<PhotoBatch, PhotoBatch> chunk(10)
                .reader(uploadedFileReader)
                .processor(thumbnailProcessor)
                .writer(thumbnailFileWriter)
                //.taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    Step scaleStep(UploadedFileReader uploadedFileReader,
                   ScalingProcessor scalingProcessor,
                   ScaledFileWriter scaledFileWriter) {

        return stepBuilderFactory.get("scaleStep")
                .<PhotoBatch, PhotoBatch> chunk(10)
                .reader(uploadedFileReader)
                .processor(scalingProcessor)
                .writer(scaledFileWriter)
                //.taskExecutor(taskExecutor)
                .build();
    }
}
