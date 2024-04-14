package org.bai.security.library;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

public class AppProperties {
    private static final String CONFIG_FILE = "application.yaml";
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(new File(AppProperties.class.getClassLoader().getResource(CONFIG_FILE).toURI()))) {
            final Yaml yaml = new Yaml();
            Map<String, Object> yamlData = yaml.load(fis);
            if (yamlData != null) {
                buildProperties("", yamlData, properties);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private static void buildProperties(String prefix, Map<String, Object> data, Properties properties) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() instanceof Map) {
                buildProperties(prefix + entry.getKey() + ".", (Map<String, Object>) entry.getValue(), properties);
            } else {
                properties.setProperty(prefix + entry.getKey(), entry.getValue().toString());
            }
        }
    }

    public static String getProperty(final String key) {
        return properties.getProperty(key);
    }

    private AppProperties() {}
}