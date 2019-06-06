package com.ethanaa.photo.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import com.ethanaa.photo.config.Profiles;
import com.ethanaa.photo.config.SpacesProperties;
import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.model.PhotoType;
import com.ethanaa.photo.model.exception.PhotoDeleteException;
import com.ethanaa.photo.model.exception.PhotoWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Profile(Profiles.DIGITAL_OCEAN)
public class SpacesStorageService implements PhotoStorageService {

    private static final Logger LOG = LoggerFactory.getLogger(SpacesStorageService.class);

    private AmazonS3 s3Client;
    private TransferManager transferManager;
    private SpacesProperties spacesProperties;

    @Autowired
    public SpacesStorageService(AmazonS3 s3Client,
                                TransferManager transferManager,
                                SpacesProperties spacesProperties) {

        this.s3Client = s3Client;
        this.transferManager = transferManager;
        this.spacesProperties = spacesProperties;
    }

    @Override
    public void write(Photo photo, PhotoType photoType) throws PhotoWriteException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(photo.getImage(photoType), photo.getExtension(), os);
        } catch (IOException ioe) {
            throw new PhotoWriteException(photo, photoType, "Failed to write to output stream");
        }

        byte[] buffer = os.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(buffer);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(buffer.length);
        objectMetadata.setContentType(photo.getContentType());

        Upload upload = transferManager.upload(new PutObjectRequest(
                spacesProperties.getRootBucket(),
                photo.getPath(photoType),
                inputStream,
                objectMetadata
                ));

        try {
            UploadResult uploadResult = upload.waitForUploadResult();
        } catch (Exception e) {
            throw new PhotoWriteException(photo, photoType, e);
        }
    }

    @Override
    public void delete(Photo photo, PhotoType photoType) throws PhotoDeleteException {

        try {

            s3Client.deleteObject(spacesProperties.getRootBucket(), photo.getPath(photoType));

        } catch (AmazonServiceException ase) {
            throw new PhotoDeleteException(photo, photoType, ase);
        }
    }

    @Override
    public void delete(Photo photo) throws PhotoDeleteException {

        try {

            List<String> paths = photo.getPaths();

            DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(spacesProperties.getRootBucket())
                    .withKeys(photo.getPaths().toArray(new String[paths.size()]));

            s3Client.deleteObjects(deleteRequest);

        } catch (AmazonServiceException ase) {
            throw new PhotoDeleteException(photo, ase);
        }
    }

    @Override
    public void deleteAll() throws PhotoDeleteException {

        String rootBucket = spacesProperties.getRootBucket();

        try {

            ObjectListing bucketListing = s3Client.listObjects(rootBucket);

            while (true) {

                for (S3ObjectSummary summary : bucketListing.getObjectSummaries()) {
                    s3Client.deleteObject(rootBucket, summary.getKey());
                }

                // page through the object listing
                if (bucketListing.isTruncated()) {
                    bucketListing = s3Client.listNextBatchOfObjects(bucketListing);
                } else {
                    break;
                }
            }

        } catch (AmazonServiceException ase) {
            throw new PhotoDeleteException(rootBucket, ase);
        }
    }
}
