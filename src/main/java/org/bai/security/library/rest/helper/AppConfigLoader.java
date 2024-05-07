package org.bai.security.library.rest.helper;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class AppConfigLoader {

  private AppConfigLoader() {
    throw new IllegalStateException("Utility class");
  }
  public static FileUploadConfig loadConfig() {
    Yaml yaml = new Yaml(new Constructor(FileUploadConfig.class, new LoaderOptions()));
    try (InputStream in = Files.newInputStream(Paths.get("src/main/resources/application.yaml"))) {
      return yaml.load(in);
    } catch (Exception e) {
      log.info(e.getMessage());
    return null;

    }
  }
}
