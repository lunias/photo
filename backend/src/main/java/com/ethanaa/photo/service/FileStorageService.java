package com.ethanaa.photo.service;

import com.ethanaa.photo.config.PhotoProperties;
import com.ethanaa.photo.config.Profiles;
import com.ethanaa.photo.model.Photo;
import com.ethanaa.photo.model.PhotoType;
import com.ethanaa.photo.model.exception.PhotoDeleteException;
import com.ethanaa.photo.model.exception.PhotoWriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Profile("!" + Profiles.DIGITAL_OCEAN)
public class FileStorageService implements PhotoStorageService {

    private PhotoProperties photoProperties;

    @Autowired
    public FileStorageService(PhotoProperties photoProperties) {

        this.photoProperties = photoProperties;
    }

    @Override
    public void write(Photo photo, PhotoType photoType) throws PhotoWriteException {

        if (photo.getImage(photoType) == null) {
            throw new PhotoWriteException(photo, photoType, "Null " + photoType + " image");
        }

        try {
            ImageIO.write(photo.getImage(photoType), photo.getExtension(), getFile(photo, photoType));
        } catch (IOException ioe) {
            throw new PhotoWriteException(photo, photoType, ioe);
        }
    }

    @Override
    public void delete(Photo photo, PhotoType photoType) throws PhotoDeleteException {

        try {
            File photoFile = new File(photo.getPath(photoType));
            photoFile.delete();
        } catch (Exception e) {
            throw new PhotoDeleteException(photo, photoType, e);
        }
    }

    @Override
    public void delete(Photo photo) throws PhotoDeleteException {

        List<String> paths = photo.getPaths();
        for (String path : paths) {
            try {
                File photoFile = new File(path);
                photoFile.delete();
            } catch (Exception e) {
                throw new PhotoDeleteException(photo, e);
            }
        }
    }

    @Override
    public void deleteAll() throws PhotoDeleteException {

        String outputDirectory = photoProperties.getOutputDir();

        try {
            Files.walk(Paths.get(outputDirectory))
                    .map(Path::toFile)
                    .sorted((o1, o2) -> -o1.compareTo(o2))
                    .forEach(File::delete);
        } catch (IOException ioe) {
            throw new PhotoDeleteException(outputDirectory, ioe);
        }
    }

    private File getFile(Photo photo, PhotoType type) {

        File file = new File(photo.getPath(type));
        File parentDirectory = file.getParentFile();

        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }

        return file;
    }
}
