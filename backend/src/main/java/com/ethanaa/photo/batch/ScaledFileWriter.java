package com.ethanaa.photo.batch;

import com.ethanaa.photo.model.PhotoData;
import com.ethanaa.photo.repository.PhotoDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.List;

@Component
@StepScope
public class ScaledFileWriter implements ItemWriter<PhotoData> {

    private static final Logger LOG = LoggerFactory.getLogger(ScaledFileWriter.class);

    private String scaledDirectory;
    private PhotoDataRepository photoDataRepository;

    @Autowired
    public ScaledFileWriter(@Value("#{jobParameters['scaledDir']}") String scaledDirectory,
                            PhotoDataRepository photoDataRepository) {

        this.scaledDirectory = scaledDirectory;
        this.photoDataRepository = photoDataRepository;
    }

    @Override
    public void write(List<? extends PhotoData> scaledDataList) throws Exception {

        LOG.info("Writing scaled files to {}", scaledDirectory);

        File file = new File(scaledDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }

        for (PhotoData scaledData : scaledDataList) {

            ImageIO.write(scaledData.getBufferedImage(), scaledData.getExtension(),
                    scaledData.createScaledFile(scaledDirectory));

            LOG.info("Wrote scaled {}", scaledData);
        }

        photoDataRepository.saveAll(scaledDataList);
    }
}
