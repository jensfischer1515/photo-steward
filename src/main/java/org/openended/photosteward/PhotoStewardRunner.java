package org.openended.photosteward;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Production
@RequiredArgsConstructor
public class PhotoStewardRunner implements ApplicationRunner {

    private final PhotoStewardConfiguration configuration;

    @Override
    public void run(ApplicationArguments args) {

        log.debug("Running with cfg {}", configuration);

        new PhotoSteward(configuration.getAbsoluteSourcePath(), configuration.getAbsoluteDestinationPath(), configuration.isMove()).run();
    }
}
