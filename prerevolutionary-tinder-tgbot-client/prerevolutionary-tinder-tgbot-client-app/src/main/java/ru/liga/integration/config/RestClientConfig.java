package ru.liga.integration.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import ru.liga.model.ServiceUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class RestClientConfig {

    private static final RestClientConfig INSTANCE = new RestClientConfig();
    private final Logger logger = LoggerFactory.getLogger(RestClientConfig.class);
    private final Properties properties = new Properties();

    public RestClientConfig() {
        final String propertiesPath = "application.properties";
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

    public static RestClientConfig getInstance() {
        return INSTANCE;
    }

    @Bean
    public RestTemplate restTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    public String getRegisterServiceUrl() {
        return properties.getProperty("register.service.url");
    }

    @Bean
    public ServiceUser serviceUser() {
        return new ServiceUser(properties.getProperty("service.user.name"),
                properties.getProperty("service.user.password"));
    }

    private void handlePropertiesLoadFailure(String path) {
        final String errorMessage = getPropertiesPathErrorMessage() + path;
        logger.error(errorMessage);
        throw new RuntimeException(getPropertiesPathErrorMessage() + path);
    }

    private void handlePropertiesLoadException(String path, IOException e) {
        final String errorMessage = getPropertiesPathErrorMessage() + path;
        logger.error(errorMessage, e);
        throw new RuntimeException(errorMessage, e);
    }

    private String getPropertiesPathErrorMessage() {
        return "Failed to load application properties from ";
    }
}
