package org.bai.security.library.rest.helper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUploadConfig {
  private int fileSizeThreshold;
  private int maxFileSize;
  private int maxRequestSize;
}