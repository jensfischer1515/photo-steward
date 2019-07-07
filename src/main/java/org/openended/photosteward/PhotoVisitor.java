package org.openended.photosteward;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashMap;
import java.util.Map;

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
            new PhotoMover(destination).movePhoto(photo)
                    .ifPresent(path -> log.info("Moved {} to {}", photo.getPath(), path));
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
