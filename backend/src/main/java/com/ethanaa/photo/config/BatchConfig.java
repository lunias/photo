package com.ethanaa.photo.config;

import com.ethanaa.photo.batch.*;
import com.ethanaa.photo.model.Photo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.util.Arrays;

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
    Job uploadJob(Step processPhoto) {

        return jobBuilderFactory.get("uploadJob")
                .incrementer(new RunIdIncrementer())
                .start(processPhoto)
                .build();
    }

    @Bean
    Step processPhoto(UploadedPhotoReader uploadedPhotoReader,
                       ThumbnailProcessor thumbnailProcessor,
                       ScalingProcessor scalingProcessor,
                       ThumbnailPhotoWriter thumbnailPhotoWriter,
                       ScaledPhotoWriter scaledPhotoWriter,
                       RawFileWriter rawFileWriter) {

        CompositeItemProcessor<Photo, Photo> photoProcessorChain = new CompositeItemProcessor<>();
        photoProcessorChain.setDelegates(Arrays.asList(thumbnailProcessor, scalingProcessor));

        CompositeItemWriter<Photo> photoWriterChain = new CompositeItemWriter<>();
        photoWriterChain.setDelegates(Arrays.asList(thumbnailPhotoWriter, scaledPhotoWriter, rawFileWriter));

        return stepBuilderFactory.get("processPhotos")
                .<Photo, Photo> chunk(5)
                .reader(uploadedPhotoReader)
                .processor(photoProcessorChain)
                .writer(photoWriterChain)
                .taskExecutor(taskExecutor)
                .build();
    }
}
