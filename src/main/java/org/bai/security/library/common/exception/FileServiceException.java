package org.bai.security.library.common.exception;

public class FileServiceException extends RuntimeException {
    public FileServiceException(final String msg, final Exception e) {
        super(msg, e);
    }
}
