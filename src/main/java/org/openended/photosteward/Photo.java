package org.openended.photosteward;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.file.FileTypeDirectory;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.drew.imaging.ImageMetadataReader.readMetadata;
import static java.util.stream.StreamSupport.stream;

@Slf4j
@ToString(exclude = "metadata")
@RequiredArgsConstructor
public class Photo {

    @Getter
    private final Path path;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<Metadata> metadata;

    public Optional<Date> getDate() {
        return getMetadata().flatMap(this::findDate);
    }

    public Optional<String> getMimeType() {
        return getMetadata().flatMap(this::findMimeType);
    }

    private Optional<Metadata> getMetadata() {
        //noinspection OptionalAssignedToNull
        if (metadata == null) {
            metadata = Try.of(() -> readMetadata(path.toFile()))
                    .onFailure(e -> log.warn("Could not read metadata for {}", this))
                    .toJavaOptional();
        }
        return metadata;
    }

    private Optional<String> findMimeType(Metadata metadata) {
        return metadata.getDirectoriesOfType(FileTypeDirectory.class).stream()
                .map(directory -> directory.getString(FileTypeDirectory.TAG_DETECTED_FILE_MIME_TYPE))
                .findFirst();
    }

    private Optional<Date> findDate(Metadata metadata) {
        return stream(metadata.getDirectories().spliterator(), false)
                .filter(((Predicate<Directory>) Directory::hasErrors).negate())
                .map(this::findDate)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted()
                .findFirst();
    }

    private Optional<Date> findDate(Directory directory) {
        return Stream.<Supplier<Date>>of(
                () -> directory.getDate(ExifDirectoryBase.TAG_DATETIME_ORIGINAL),
                () -> directory.getDate(ExifDirectoryBase.TAG_DATETIME_DIGITIZED),
                () -> directory.getDate(ExifDirectoryBase.TAG_DATETIME),
                () -> directory.getDate(FileSystemDirectory.TAG_FILE_MODIFIED_DATE))
                .map(Supplier::get)
                .filter(Objects::nonNull)
                .findFirst();
    }
}
