package org.bai.security.library.rest.helper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@Singleton
public class FilesServiceProvider {
    private final FilesHelper filesHelper = new FilesHelper();

    @Produces
    @FileService
    @ApplicationScoped
    public FilesHelper filesHelper() {
        return filesHelper;
    }

    public void close(@Disposes @FileService FilesHelper fh) {
        fh.cleanUpService();
    }

    private FilesServiceProvider() {
    }
}