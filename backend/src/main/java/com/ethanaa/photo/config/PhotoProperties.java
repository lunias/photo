package com.ethanaa.photo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Configuration
@ConfigurationProperties(prefix = "photo")
public class PhotoProperties {

    @NotBlank
    private String outputDir;

    public String getOutputDir() {

        return outputDir;
    }

    public void setOutputDir(String outputDir) {

        this.outputDir = outputDir;
    }
}
