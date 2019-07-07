package org.openended.photosteward;

import lombok.Data;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import static java.nio.file.Files.isDirectory;

@Data
@ConfigurationProperties(prefix = "photosteward")
public class PhotoStewardConfiguration implements InitializingBean {
    private String source;
    private String destination;
    private boolean move;

    public Path getAbsoluteSourcePath() {
        return FileSystems.getDefault().getPath(source).toAbsolutePath();
    }

    public Path getAbsoluteDestinationPath() {
        return FileSystems.getDefault().getPath(destination).toAbsolutePath();
    }

    @Override
    public void afterPropertiesSet() {
        if (!isDirectory(getAbsoluteSourcePath())) {
            throw new BeanCreationException(String.format("source dir '%s' is not a directory", getAbsoluteSourcePath()));
        }
    }
}
