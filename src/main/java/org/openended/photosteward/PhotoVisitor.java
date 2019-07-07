package org.openended.photosteward;

import io.vavr.control.Try;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.move;
import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.time.ZoneOffset.UTC;

@Slf4j
@RequiredArgsConstructor
public class PhotoVisitor implements FileVisitor<Path> {

    private final Path destination;

    private final boolean move;

    @Getter
    private final Map<Path, IOException> errors = new LinkedHashMap<>();

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        log.debug("preVisitDirectory {}", dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        log.debug("visitFile {}", file);
        Photo photo = new Photo(file);
        log.info("photo {}", photo);

        if (move && photo.isImage()) {
            photo.getDate().ifPresent(date -> {
                OffsetDateTime dateTime = date.toInstant().atOffset(UTC);
                int year = dateTime.getYear();
                int month = dateTime.getMonthValue();
                Path destinationDir = destination.resolve(String.format("%04d", year)).resolve(String.format("%02d", month));
                Path destinationFile = destinationDir.resolve(photo.getFileName());
                Try.of(() -> createDirectories(destinationDir))
                        .onFailure(e -> log.error("Could not create dir {}", destinationDir, e))
                        .andThenTry(() -> move(photo.getPath(), destinationFile, ATOMIC_MOVE))
                        .onFailure(e -> log.error("Could not move {} to {}", photo.getPath(), destinationDir, e))
                        .onSuccess(path -> log.info("Moved {} to {}", photo.getPath(), path))
                ;
            });
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        log.debug("visitFileFailed {}", file);
        errors.put(file, exc);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        log.debug("postVisitDirectory {}", dir);
        return FileVisitResult.CONTINUE;
    }
}
