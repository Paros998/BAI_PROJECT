package org.bai.security.library.common.properties;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import lombok.Getter;
import org.bai.security.library.common.properties.config.AppConfig;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;

@Singleton
public class AppProperties {
    private static final String CONFIG_FILE = "application.yaml";

    @Getter
    private static AppConfig properties;

    static {
        try (FileInputStream fis = new FileInputStream(new File(AppProperties.class.getClassLoader().getResource(CONFIG_FILE).toURI()))) {
            Yaml yaml = new Yaml(new Constructor(AppConfig.class, new LoaderOptions()));

            AppConfig appConfig = yaml.load(fis);
            if (appConfig != null) {
                properties = appConfig;
            }
        } catch (final Exception e) {
            throw new WebApplicationException(e);
        }
    }

    public static Long convertMegaBytesToBytes(final Long megabytes) {
        return megabytes * 1024 * 1024;
    }

    @Produces
    @ApplicationScoped
    @PropertyBasedAppState
    public AppState appState() {
        final boolean isSafetyEnabled = properties.getSafety().isEnabled();
        return AppState.builder()
                .appMode(isSafetyEnabled ? AppState.AppMode.SAFE : AppState.AppMode.UNSAFE)
                .build();
    }

    @Produces
    @ApplicationScoped
    @PropertyBasedFilesConfig
    public FilesConfig filesConfig() {
        final String[] defaultBookPhoto = properties.getFiles().getBookFileName().split("\\.");
        long filesMaxSize = convertMegaBytesToBytes((long) properties.getFiles().getFileUpload().getMaxFileSize());
        return FilesConfig.builder()
                .maxFileSizeInBytes(filesMaxSize)
                .defaultBookPhotoFileName(defaultBookPhoto[0])
                .defaultBookPhotoFileExtension(defaultBookPhoto[1])
                .build();
    }

    private AppProperties() {}
}