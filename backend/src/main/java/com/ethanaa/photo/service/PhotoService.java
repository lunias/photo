package com.ethanaa.photo.service;

import com.ethanaa.photo.config.PhotoProperties;
import com.ethanaa.photo.model.PhotoData;
import com.ethanaa.photo.repository.PhotoDataRepository;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.ethanaa.photo.model.PhotoData.Type.RAW;

@Service
public class PhotoService {

    private static final Logger LOG = LoggerFactory.getLogger(PhotoService.class);

    private final String outputDir;

    private JobLauncher jobLauncher;
    private Job uploadJob;

    private PhotoDataRepository photoDataRepository;

    @Autowired
    public PhotoService(JobLauncher jobLauncher,
                        Job uploadJob,
                        PhotoDataRepository photoDataRepository,
                        PhotoProperties photoProperties) {

        this.jobLauncher = jobLauncher;
        this.uploadJob = uploadJob;

        this.photoDataRepository = photoDataRepository;

        this.outputDir = photoProperties.getOutputDir();
    }

    private String getDirectory(String username, String batchId, PhotoData.Type type, String uploadId) {

        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        return outputDir + username +
                "/" + today +
                "/" + batchId +
                "/" + type +
                "/" + (!StringUtils.isEmpty(uploadId) ? uploadId + "/" : "");
    }

    private String getDirectory(String username, String batchId, PhotoData.Type type) {

        return getDirectory(username, batchId, type, null);
    }

    public File createUploadDirectory(Authentication authentication, String batchId, String uploadId) {

        File uploadDirectory = new File(getDirectory(authentication.getName(), batchId, PhotoData.Type.RAW, uploadId));
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        return uploadDirectory;
    }

    public void deleteAllPhotoData() {

        LOG.info("Deleting all uploaded photos");

        try {
            deleteOutputDirectory();
        } catch (IOException ioe) {
            LOG.warn("Failed to delete output directory", ioe);
        }

        photoDataRepository.deleteAll();
    }

    public void deleteOutputDirectory() throws IOException {

        deleteDirectory(outputDir);
    }

    private void deleteDirectory(String directory) throws IOException {

        try {

            Files.walk(Paths.get(directory))
                    .map(Path::toFile)
                    .sorted((o1, o2) -> -o1.compareTo(o2))
                    .forEach(File::delete);

        } catch (NoSuchFileException nsfe) {
            LOG.debug("File does not exist", nsfe);
        }
    }

    @Async
    public void transferToDirectory(MultipartFile photoFile, File directory, int index,
                                    CompletableFuture<Integer> transferFuture) throws IOException {

        String originalName = StringUtils.cleanPath(photoFile.getOriginalFilename());

        if (photoFile.isEmpty()) {
            LOG.error("Cannot transfer empty file {}", originalName);
        }

        if (originalName.contains("..")) {
            LOG.error("Cannot transfer file with relative path outside current directory {}", originalName);
        }

        File uploadDirectory = new File(directory + "/" + originalName);
        if (originalName.contains("/") && !uploadDirectory.getParentFile().exists()) {
            uploadDirectory.getParentFile().mkdirs();
        }

        photoFile.transferTo(uploadDirectory);

        transferFuture.complete(index);
    }

    public void runUploadJob(Authentication authentication, String batchId, String uploadId)
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {

        jobLauncher.run(uploadJob, new JobParameters(new HashMap<String, JobParameter>() {
            {
                put("uploadId", new JobParameter(UUID.randomUUID().toString(), true));
                put("batchId", new JobParameter(batchId, false));
                put("rawDir", new JobParameter(getDirectory(authentication.getName(), batchId, RAW, uploadId), false));
                put("thumbDir", new JobParameter(getDirectory(authentication.getName(), batchId, PhotoData.Type.THUMBNAIL), false));
                put("scaledDir", new JobParameter(getDirectory(authentication.getName(), batchId, PhotoData.Type.SCALED), false));
            }
        }));
    }
}
