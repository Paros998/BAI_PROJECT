package org.bai.security.library.entity.files;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bai.security.library.api.files.FileDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileMapper {
    public static FileDto toFileDto(final @NonNull FileEntity file) {
        return FileDto.builder()
                .fileName(file.getFileName())
                .extension(file.getExtension())
                .content(file.getContent())
                .build();
    }
}