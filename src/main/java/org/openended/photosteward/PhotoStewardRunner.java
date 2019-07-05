package org.openended.photosteward;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class PhotoStewardRunner implements ApplicationRunner {

    private final PhotoStewardConfiguration configuration;

    @Override
    @SneakyThrows
    public void run(ApplicationArguments args) {

        log.info("Running with cfg {}", configuration);
        log.info("Running with args {}", args);


        //Path source = configuration.getAbsoluteSourcePath().iterator();
        //log.info("source {}", source);


        Metadata metadata = ImageMetadataReader.readMetadata(configuration.getAbsoluteSourcePath().resolve("IMG_3892.jpg").toFile());
        ExifSubIFDDirectory exifSubIFD0 = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        Instant dateOriginal = exifSubIFD0.getDateOriginal().toInstant();


        FileSystemDirectory fileSystem = metadata.getFirstDirectoryOfType(FileSystemDirectory.class);
        Instant fileModified = fileSystem.getDate(FileSystemDirectory.TAG_FILE_MODIFIED_DATE).toInstant();

        log.info("Metadata {}", metadata);
    }
}
