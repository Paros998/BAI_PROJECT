package org.bai.security.library.common.properties;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

@Singleton
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

    /* ----------------------------------------------------------- */

    public static final String JWT_SECRET_PROPERTY = "jwt.secret";
    public static final String APP_SAFETY_MODE = "app.safety.enabled";

    /* ----------------------------------------------------------- */

    public static String getProperty(final String key) {
        return properties.getProperty(key);
    }

    @Produces
    @ApplicationScoped
    @PropertyBasedAppState
    public AppState appState() {
        final boolean isSafetyEnabled = Boolean.parseBoolean(getProperty(APP_SAFETY_MODE));
        return AppState.builder()
                .appMode(isSafetyEnabled ? AppState.AppMode.SAFE : AppState.AppMode.UNSAFE)
                .build();
    }

    private AppProperties() {}
}