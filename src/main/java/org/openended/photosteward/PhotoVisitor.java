package org.openended.photosteward;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class PhotoVisitor extends SimpleFileVisitor<Path> {

    private final Path destination;

    private final boolean move;


    private AtomicInteger processed = new AtomicInteger(0);

    @Getter
    private final Map<Path, IOException> errors = new LinkedHashMap<>();

    public int getProcessed() {
        return processed.intValue();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        log.debug("visitFile {}", file);
        Photo photo = new Photo(file);
        log.info("photo {}", photo);

        if (move && photo.isImage()) {
            Optional<Path> moved = new PhotoMover(destination).movePhoto(photo);
            if (moved.isPresent()) {
                log.info("Moved {} to {}", photo.getPath(), moved.get());
                processed.incrementAndGet();
            } else {
                errors.put(file, null);
            }
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        log.debug("visitFileFailed {}", file);
        errors.put(file, exc);
        return FileVisitResult.CONTINUE;
    }
}
