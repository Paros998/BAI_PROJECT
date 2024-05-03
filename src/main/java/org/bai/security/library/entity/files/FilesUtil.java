package org.bai.security.library.entity.files;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bai.security.library.common.exception.FileNotFoundException;

import java.io.File;
import java.net.URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilesUtil {

    public static File loadFile(final @NonNull String fileName) throws FileNotFoundException {
        final URL resource = FilesUtil.class.getClassLoader().getResource("files/" + fileName);
        if (resource == null) {
            throw new FileNotFoundException();
        }

        try {
            final var uri = resource.toURI();
            return new File(uri);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new FileNotFoundException(e);
        }
    }
}