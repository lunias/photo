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
public class ThumbnailFileWriter implements ItemWriter<PhotoData> {

    private static final Logger LOG = LoggerFactory.getLogger(ThumbnailFileWriter.class);

    private String thumbnailDirectory;
    private PhotoDataRepository photoDataRepository;

    @Autowired
    public ThumbnailFileWriter(@Value("#{jobParameters['thumbnailDirectory']}") String thumbnailDirectory,
                               PhotoDataRepository photoDataRepository) {

        this.thumbnailDirectory = thumbnailDirectory;
        this.photoDataRepository = photoDataRepository;
    }

    @Override
    public void write(List<? extends PhotoData> thumbnailDataList) throws Exception {

        LOG.info("Writing thumbnail files to {}", thumbnailDirectory);

        File file = new File(thumbnailDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }

        for (PhotoData thumbnailData : thumbnailDataList) {

            ImageIO.write(thumbnailData.getBufferedImage(), thumbnailData.getExtension(),
                    thumbnailData.createThumbnailFile(thumbnailDirectory));

            LOG.info("Wrote thumbnail {}", thumbnailData);
        }

        photoDataRepository.saveAll(thumbnailDataList);
    }
}
