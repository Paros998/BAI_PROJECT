package org.bai.security.library.entity.files;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import lombok.NonNull;
import org.bai.security.library.business.BusinessExceptionFactory;
import org.bai.security.library.common.properties.FilesConfig;
import org.bai.security.library.common.properties.PropertyBasedFilesConfig;
import org.bai.security.library.domain.files.FileDto;
import org.bai.security.library.domain.files.FileMapper;
import org.bai.security.library.domain.files.FileRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class FileEntityRepository implements FileRepository {
    private final FilesConfig filesConfig;

    @RequestScoped
    private final EntityManager em;

    @Inject
    public FileEntityRepository(final @PropertyBasedFilesConfig FilesConfig filesConfig,
                                final EntityManager em) {
        this.filesConfig = filesConfig;
        this.em = em;

        initializeDefaults();
    }

    private void initializeDefaults() {
        if (getFileByFileName(filesConfig.getDefaultBookPhotoFileName()).isPresent()) {
            return;
        }

        final String fileNameWithExtension
                = String.format("%s.%s", filesConfig.getDefaultBookPhotoFileName(), filesConfig.getDefaultBookPhotoFileExtension());
        final File file = FilesUtil.loadFile(fileNameWithExtension);
        saveFile(file);
    }

    @Override
    public UUID saveFile(final @NonNull File file) {
        if (!file.isFile() && !file.canRead()) {
            throw BusinessExceptionFactory.forMessage("Error during file saving.");
        }

        final String[] fileName = file.getName().split("\\.");
        final FileEntity fileEntity = FileEntity.builder()
                .fileName(fileName[0])
                .extension(fileName[1])
                .build();
        final EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        try (final var in = new FileInputStream(file.getPath())) {
            validateFileSize(in);
            fileEntity.setContent(in.readAllBytes());
            em.persist(fileEntity);
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw BusinessExceptionFactory.forMessage("Error during file saving.");
        }

        return fileEntity.getFileId();
    }

    private void validateFileSize(FileInputStream in) throws IOException {
        if (in.available() > filesConfig.getMaxFileSizeInBytes()) {
            throw BusinessExceptionFactory.forMessage("File size exceeds the max limit.");
        }
    }

    @Override
    public Optional<FileEntity> findFileEntityById(final @NonNull UUID fileId) {
        return Optional.ofNullable(em.find(FileEntity.class, fileId));
    }

    @Override
    public Optional<FileEntity> getFileByFileName(final @NonNull String fileName) {
        try {
            return Optional.of(
                    em.createQuery("select f from files f where f.fileName = :fileName", FileEntity.class)
                            .setParameter("fileName", fileName)
                            .getSingleResult()
            );
        } catch (final NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public FileDto getFile(final @NonNull UUID fileId) {
        return findFileEntityById(fileId)
                .map(FileMapper::toFileDto)
                .orElseThrow(() -> BusinessExceptionFactory.forMessage(String.format("File with id: [%s] not found.", fileId)));

    }
}