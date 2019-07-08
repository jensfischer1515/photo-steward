package org.openended.photosteward;

import com.github.marschall.memoryfilesystem.MemoryFileSystemFactoryBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.nio.file.FileSystem;

@TestConfiguration
public class MemoryFileSystemConfiguration {

    @Bean
    public MemoryFileSystemFactoryBean memoryFileSystemFactoryBean() {
        MemoryFileSystemFactoryBean factoryBean = new MemoryFileSystemFactoryBean();
        factoryBean.setType(MemoryFileSystemFactoryBean.MACOS);
        return factoryBean;
    }

    @Primary
    @Bean(destroyMethod = "close")
    public FileSystem memoryFileSystem() {
        return memoryFileSystemFactoryBean().getObject();
    }
}
