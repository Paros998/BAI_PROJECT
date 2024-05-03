package org.bai.security.library.domain.files;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bai.security.library.entity.files.FileEntity;

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