package com.ethanaa.photo.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Profile(Profiles.DIGITAL_OCEAN)
@Configuration
@EnableConfigurationProperties
public class S3ClientConfig {

    private static final Logger LOG = LoggerFactory.getLogger(S3ClientConfig.class);

    private AmazonS3 s3Client;

    private SpacesProperties spacesProperties;

    @Autowired
    public S3ClientConfig(SpacesProperties spacesProperties) {

        this.spacesProperties = spacesProperties;
    }

    @Bean
    AWSCredentials credentials() {

        return new BasicAWSCredentials(
                spacesProperties.getAccessKey(), spacesProperties.getSecretKey());
    }

    @Bean
    AmazonS3 s3Client(AWSCredentials credentials) {

        this.s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(
                        credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        spacesProperties.getServiceEndpoint(), spacesProperties.getSigningRegion()))
                .build();

        return s3Client;
    }

    @Bean
    TransferManager transferManager(AmazonS3 s3Client) {

        return TransferManagerBuilder.standard()
                .withS3Client(s3Client)
                .build();
    }

    @PostConstruct
    void initPhotoBucket() {

        String rootBucket = spacesProperties.getRootBucket();

        if (s3Client.doesBucketExistV2(rootBucket)) {
            LOG.debug("Root bucket {} exists");
            return;
        }

        try {
            s3Client.createBucket(rootBucket);
        } catch (Exception e) {
            LOG.error("Failed to create root bucket: " + rootBucket, e);
            System.exit(1);
        }
    }
}
