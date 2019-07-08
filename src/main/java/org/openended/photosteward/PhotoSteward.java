package org.openended.photosteward;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

import static java.nio.file.Files.walkFileTree;

@Slf4j
@RequiredArgsConstructor
public class PhotoSteward {

    private final Path source;

    private final Path destination;

    private final boolean move;

    public void run() {
        PhotoVisitor visitor = new PhotoVisitor(destination, move);
        Path startingPath = Try.of(() -> walkFileTree(source, visitor)).get();
        log.info("Processed {} images in {} with errors: {}", visitor.getProcessed(), startingPath, visitor.getErrors());
    }
}
