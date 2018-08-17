package com.ethanaa.photo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.validation.constraints.NotBlank;

@Profile(Profiles.DIGITAL_OCEAN)
@Configuration
@ConfigurationProperties(prefix = "spaces")
public class SpacesProperties {

    private String serviceEndpoint = "https://nyc3.digitaloceanspaces.com";

    private String signingRegion = "nyc3";

    @NotBlank
    private String accessKey;

    @NotBlank
    private String secretKey;

    private String rootBucket = "photo";

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    public String getSigningRegion() {
        return signingRegion;
    }

    public void setSigningRegion(String signingRegion) {
        this.signingRegion = signingRegion;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRootBucket() {
        return rootBucket;
    }

    public void setRootBucket(String rootBucket) {
        this.rootBucket = rootBucket;
    }
}
