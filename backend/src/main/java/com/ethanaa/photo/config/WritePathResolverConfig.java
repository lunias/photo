package com.ethanaa.photo.config;

import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.model.PhotoBatch;
import com.ethanaa.photo.model.PhotoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.function.BiFunction;

@Configuration
@EnableConfigurationProperties
public class WritePathResolverConfig {

    private PhotoProperties photoProperties;

    @Autowired
    public WritePathResolverConfig(PhotoProperties photoProperties) {

        this.photoProperties = photoProperties;
    }

    @Bean
    @Profile("!" + Profiles.DIGITAL_OCEAN)
    BiFunction<Photo, PhotoType, String> filePathFunction() {

        return (photo, photoType) -> {

            PhotoBatch photoBatch = photo.getPhotoBatch();

            StringBuilder sb = new StringBuilder(photoProperties.getOutputDir())
                    .append(photoBatch.getId().getUsername())
                    .append("/")
                    .append(photoBatch.getCreatedAt().toLocalDate())
                    .append("/")
                    .append(photoBatch.getId().getId())
                    .append("/")
                    .append(photoType)
                    .append("/")
                    .append(photo.getOriginalFilename());

            return sb.toString();
        };
    }

    @Bean
    @Profile(Profiles.DIGITAL_OCEAN)
    BiFunction<Photo, PhotoType, String> spacesBucketFunction(SpacesProperties spacesProperties) {

        return (photo, photoType) -> {

            PhotoBatch photoBatch = photo.getPhotoBatch();

            StringBuilder sb = new StringBuilder(spacesProperties.getRootBucket())
                    .append(photoBatch.getId().getUsername())
                    .append("/")
                    .append(photoBatch.getCreatedAt().toLocalDate())
                    .append("/")
                    .append(photoBatch.getId().getId())
                    .append("/")
                    .append(photo.getOriginalFilename());

            return sb.toString();
        };
    }
}
