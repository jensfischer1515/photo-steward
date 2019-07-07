package org.openended.photosteward;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static java.nio.file.Files.walkFileTree;

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

        walkFileTree(configuration.getAbsoluteSourcePath(), new PhotoVisitor(configuration.getAbsoluteDestinationPath(), configuration.isMove()));
    }
}
