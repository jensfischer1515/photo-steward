package org.openended.photosteward;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.move;
import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.time.ZoneOffset.UTC;

@Slf4j
@RequiredArgsConstructor
public class PhotoMover {

    private final Path destination;

    public Optional<Path> movePhoto(Photo photo) {
        return photo.getDate()
                .map(Date::toInstant)
                .map(instant -> instant.atOffset(UTC))
                .map(this::destinationDir)
                .map(destinationDir -> {
                    Path destinationFile = destinationDir.resolve(photo.getFileName());
                    return Try.of(() -> createDirectories(destinationDir))
                            .onFailure(e -> log.error("Could not create dir {}", destinationDir, e))
                            .andThenTry(() -> move(photo.getPath(), destinationFile, ATOMIC_MOVE))
                            .onFailure(e -> log.error("Could not move {} to {}", photo.getPath(), destinationDir, e))
                            .get();

                });
    }

    private Path destinationDir(OffsetDateTime dateTime) {
        String year = String.format("%04d", dateTime.getYear());
        String month = String.format("%02d", dateTime.getMonthValue());
        return destination.resolve(year).resolve(month);
    }
}
