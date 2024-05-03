package org.bai.security.library.common.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(final Exception e) {
        super(e);
    }
}
