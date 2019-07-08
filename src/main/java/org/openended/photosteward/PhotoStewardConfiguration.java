package org.openended.photosteward;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import static java.nio.file.Files.isDirectory;

@Data
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "photosteward")
public class PhotoStewardConfiguration implements InitializingBean {

    private final FileSystem fileSystem;
    private String source;
    private String destination;
    private boolean move;

    public Path getAbsoluteSourcePath() {
        return fileSystem.getPath(source).toAbsolutePath();
    }

    public Path getAbsoluteDestinationPath() {
        return fileSystem.getPath(destination).toAbsolutePath();
    }

    @Override
    public void afterPropertiesSet() {
        if (!isDirectory(getAbsoluteSourcePath())) {
            //throw new BeanCreationException(String.format("source dir '%s' is not a directory", getAbsoluteSourcePath()));
        }
    }
}
