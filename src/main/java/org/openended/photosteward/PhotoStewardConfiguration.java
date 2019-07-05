package org.openended.photosteward;

import lombok.Data;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

@Data
@ConfigurationProperties(prefix = "photosteward")
public class PhotoStewardConfiguration implements InitializingBean {
    private String sourceDir;
    private String destinationDir;

    public Path getAbsoluteSourcePath() {
        return FileSystems.getDefault().getPath(sourceDir).toAbsolutePath();
    }

    public Path getAbsoluteDestinationPath() {
        return FileSystems.getDefault().getPath(destinationDir).toAbsolutePath();
    }

    @Override
    public void afterPropertiesSet() {
        if (!Files.isDirectory(getAbsoluteSourcePath())) {
            throw new BeanCreationException(String.format("source dir '%s' is not a directory", getAbsoluteSourcePath()));
        }
        if (!Files.isDirectory(getAbsoluteDestinationPath())) {
            throw new BeanCreationException(String.format("destination dir '%s' is not a directory", getAbsoluteDestinationPath()));
        }
    }
}
