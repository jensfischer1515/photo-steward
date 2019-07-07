package org.openended.photosteward;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@SuppressWarnings("SameParameterValue")
class MetadataExtractor {

    private final Metadata metadata;

    Optional<String> getOptionalString(Class<? extends Directory> directoryClass, int tagType) {
        return metadata.getDirectoriesOfType(directoryClass).stream()
                .map(directory -> directory.getString(tagType))
                .map(Optional::ofNullable)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    Optional<Date> getOptionalDate(Class<? extends Directory> directoryClass, int tagType) {
        return metadata.getDirectoriesOfType(directoryClass).stream()
                .map(directory -> directory.getDate(tagType))
                .map(Optional::ofNullable)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
