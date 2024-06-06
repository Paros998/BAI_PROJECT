package org.bai.security.library.rest.helper;

import lombok.NonNull;
import org.bai.security.library.business.BusinessExceptionFactory;
import org.bai.security.library.common.exception.FileServiceException;
import org.bai.security.library.common.properties.AppProperties;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FilesHelper {
    private final Path tempDir;

    protected FilesHelper() {
        try {
            tempDir = Files.createTempDirectory(AppProperties.getProperties().getFiles().getTempDirectory());
            if (!tempDir.toFile().exists()) {
                tempDir.toFile().createNewFile();
            }
        } catch (final Exception e) {
            throw new FileServiceException("Error during file service initialization", e);
        }
    }

    public File savePartsToTempFile(final @NonNull List<FormDataBodyPart> parts) {
        if (parts.isEmpty()) {
            throw BusinessExceptionFactory.forMessage("Error during file operations. No parts provided");
        }

        try {
            final String fileName = parts.get(0).getFileName().orElseThrow();
            final Path tempFile = Files.createFile(Path.of(tempDir.toString(), fileName));
            for (var p : parts) {
                Files.write(tempFile, p.getContent().readAllBytes());
            }

            return tempFile.toFile();
        } catch (final Exception e) {
            throw BusinessExceptionFactory.forMessage("Error during file operations.", e);
        }
    }

    public void cleanUpFile(final @NonNull File file) {
        new Thread(() -> deletePath(file.toPath())).start();
    }

    protected void cleanUpService() {
        deletePath(tempDir);
    }

    private void deletePath(final @NonNull Path path) {
        try {
            if (path.toFile().exists()) {
                Files.delete(path);
            }
        }  catch (final Exception e) {
            throw new FileServiceException("Error during path cleanup", e);
        }
    }

}