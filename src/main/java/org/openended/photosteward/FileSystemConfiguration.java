package org.openended.photosteward;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

@Configuration
public class FileSystemConfiguration {

    @Bean
    public FileSystem fileSystem() {
        return FileSystems.getDefault();
    }
}
