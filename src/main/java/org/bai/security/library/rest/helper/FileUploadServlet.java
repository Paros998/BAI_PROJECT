package org.bai.security.library.rest.helper;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;

@MultipartConfig
public class FileUploadServlet extends HttpServlet {
  // Initialize using loaded configuration
  @Override
  public void init() {
    FileUploadConfig config = AppConfigLoader.loadConfig();
    getServletContext().setAttribute("fileSizeThreshold", config.getFileSizeThreshold());
    getServletContext().setAttribute("maxFileSize", config.getMaxFileSize());
    getServletContext().setAttribute("maxRequestSize", config.getMaxRequestSize());
  }

  // Implement file upload handling
}
