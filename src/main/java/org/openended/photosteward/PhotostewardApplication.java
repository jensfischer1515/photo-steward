package org.openended.photosteward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(PhotoStewardConfiguration.class)
public class PhotostewardApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotostewardApplication.class, args);
    }

}
