package org.openended.photosteward;

import io.vavr.control.Try;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import static java.nio.file.Files.*;

public interface WithTestData {

    FileSystem getFileSystem();

    default Resource imageSource() {
        return new ClassPathResource("/IMG_3892.jpg");
    }

    default Path sourceDir() {
        return getFileSystem().getPath("/source");
    }

    default Path imageSourceDir() {
        return sourceDir().resolve("images");
    }

    default Path docsSourceDir() {
        return sourceDir().resolve("docs");
    }

    default Path destinationDir() {
        return getFileSystem().getPath("/destination");
    }

    default Path givenImage() {
        Path image = Try.of(() -> createDirectories(imageSourceDir()))
                .map(dir -> dir.resolve("IMG_3892.jpg"))
                .get();

        Long bytes = Try.withResources(() -> imageSource().getInputStream())
                .of(inputStream -> copy(inputStream, image))
                .get();

        return image;
    }

    default Path givenEmptyDocument() {
        return Try.of(() -> createDirectories(docsSourceDir()))
                .map(dir -> dir.resolve("document.txt"))
                .mapTry(doc -> createFile(doc))
                .get();
    }
}
