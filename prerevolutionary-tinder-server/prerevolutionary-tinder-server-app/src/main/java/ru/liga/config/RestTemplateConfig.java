package ru.liga.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Configuration
public class RestTemplateConfig {

    private static final RestTemplateConfig INSTANCE = new RestTemplateConfig();
    private final String propertiesPath = "application.properties";
    private final Properties properties = new Properties();

    public RestTemplateConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesPath)) {
            if (input != null) {
                properties.load(input);
            } else {
                handlePropertiesLoadFailure(propertiesPath);
            }
        } catch (IOException e) {
            handlePropertiesLoadException(propertiesPath, e);
        }
    }

    private void handlePropertiesLoadFailure(String path) {
        final String errorMessage = getPropertiesPathErrorMessage() + path;
        log.error(errorMessage);
        throw new RuntimeException(getPropertiesPathErrorMessage() + path);
    }

    private void handlePropertiesLoadException(String path, IOException e) {
        final String errorMessage = getPropertiesPathErrorMessage() + path;
        log.error(errorMessage, e);
        throw new RuntimeException(errorMessage, e);
    }

    private String getPropertiesPathErrorMessage() {
        return "Failed to load application properties from ";
    }


    public String getRemoteServiceUrl() {
        return properties.getProperty("translation.service.url");
    }

    public String getImageGeneratorServiceUrl() {
        return properties.getProperty("image.generator.service.url");
    }

}
