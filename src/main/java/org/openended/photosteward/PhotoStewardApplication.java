package org.openended.photosteward;

import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(PhotoStewardConfiguration.class)
public class PhotoStewardApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(PhotoStewardApplication.class)
                .headless(true)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .logStartupInfo(false)
                .build()
                .run(args);
    }
}
