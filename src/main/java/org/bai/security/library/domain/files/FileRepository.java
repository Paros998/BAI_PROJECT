package org.bai.security.library.domain.files;

import lombok.NonNull;
import org.bai.security.library.api.files.FileDto;
import org.bai.security.library.entity.files.FileEntity;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository {
    UUID saveFile(@NonNull File file);

    Optional<FileEntity> findFileEntityById(@NonNull UUID fileId);

    Optional<FileEntity> getFileByFileName(@NonNull String fileName);

    FileDto getFile(@NonNull UUID fileId);
}