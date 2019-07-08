package org.openended.photosteward;

import io.vavr.control.Try;
import lombok.Getter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.FileSystem;
import java.nio.file.Files;

import static java.nio.file.Files.list;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Import(MemoryFileSystemConfiguration.class)
public class PhotoStewardTest implements WithTestData {

    @Getter
    @Autowired
    private FileSystem fileSystem;

    private PhotoSteward photoSteward;

    @Before
    public void setup() {
        photoSteward = new PhotoSteward(sourceDir(), destinationDir(), true);
    }

    @Test
    public void should_run_photo_steward() {
        // GIVEN
        givenImage();
        givenEmptyDocument();

        // WHEN
        photoSteward.run();

        // THEN
        assertThat(Try.of(() -> list(imageSourceDir()).filter(Files::isRegularFile).count()).get()).isZero();
        assertThat(Try.of(() -> list(docsSourceDir()).filter(Files::isRegularFile).count()).get()).isOne();
        assertThat(Try.of(() -> list(destinationDir().resolve("2018").resolve("07")).filter(Files::isRegularFile).count()).get()).isOne();
    }
}
