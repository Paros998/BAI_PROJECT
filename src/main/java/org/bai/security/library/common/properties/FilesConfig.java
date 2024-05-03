package org.bai.security.library.common.properties;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PROTECTED)
public class FilesConfig {
    private String defaultBookPhotoFileName;
    private String defaultBookPhotoFileExtension;
    private long maxFileSizeInBytes;
}