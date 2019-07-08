package org.openended.photosteward;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.file.FileTypeDirectory;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.MimeType;

import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.nio.file.Files.newInputStream;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RequiredArgsConstructor
public class Photo {

    @Getter
    private final Path path;

    @Getter(lazy = true, value = PRIVATE)
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<MetadataExtractor> metadata = Try
            .withResources(() -> newInputStream(path))
            .of(ImageMetadataReader::readMetadata)
            .onFailure(e -> log.warn("Could not read metadata for {}", path, e))
            .map(MetadataExtractor::new)
            .toJavaOptional();

    public Path getFileName() {
        return getPath().getFileName();
    }

    public Optional<Date> getDate() {
        return Stream.<Supplier<Optional<Date>>>of(
                this::getExifOriginalDate,
                this::getExifDigitizedlDate,
                this::getExifDate,
                this::getModifiedDate)
                .map(Supplier::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    public boolean isImage() {
        return getMimeType().filter(mimeType -> mimeType.isCompatibleWith(MimeType.valueOf("image/*"))).isPresent();
    }

    public Optional<MimeType> getMimeType() {
        return getMetadata().flatMap(m -> m.getOptionalString(FileTypeDirectory.class, FileTypeDirectory.TAG_DETECTED_FILE_MIME_TYPE)).map(MimeType::valueOf);
    }

    public Optional<Date> getExifOriginalDate() {
        return getMetadata().flatMap(m -> m.getOptionalDate(ExifDirectoryBase.class, ExifDirectoryBase.TAG_DATETIME_ORIGINAL));
    }

    public Optional<Date> getExifDigitizedlDate() {
        return getMetadata().flatMap(m -> m.getOptionalDate(ExifDirectoryBase.class, ExifDirectoryBase.TAG_DATETIME_DIGITIZED));
    }

    public Optional<Date> getExifDate() {
        return getMetadata().flatMap(m -> m.getOptionalDate(ExifDirectoryBase.class, ExifDirectoryBase.TAG_DATETIME));
    }

    public Optional<Date> getModifiedDate() {
        return getMetadata().flatMap(m -> m.getOptionalDate(FileSystemDirectory.class, FileSystemDirectory.TAG_FILE_MODIFIED_DATE));
    }

    @Override
    public String toString() {
        ToStringCreator creator = new ToStringCreator(this).append("path", this.path);
        getMimeType().ifPresent(s -> creator.append("mimeType", s));
        getExifOriginalDate().ifPresent(s -> creator.append("exifOriginalDate", s));
        getExifDigitizedlDate().ifPresent(s -> creator.append("exifDigitizedDate", s));
        getExifDate().ifPresent(s -> creator.append("exifDate", s));
        getModifiedDate().ifPresent(s -> creator.append("modifiedDate", s));
        return creator.toString();
    }
}
