package org.bai.security.library.common.properties.config;

import lombok.Data;

@Data
public class AppConfig {
    private Jwt jwt;
    private Safety safety;
    private Books books;
    private Files files;

    @Data
    public static class Jwt {
        private String secret;
    }

    @Data
    public static class Safety {
        private boolean enabled;
    }

    @Data
    public static class Books {
        // In Days
        private int lendTime;
    }

    @Data
    public static class Files {
        private String tempDirectory;
        private String bookFileName;
        private String bookFilePath;
        private FileUpload fileUpload;
    }

    @Data
    public static class FileUpload {
        private int fileSizeThreshold;
        private int maxFileSize;
        private int maxRequestSize;
    }
}