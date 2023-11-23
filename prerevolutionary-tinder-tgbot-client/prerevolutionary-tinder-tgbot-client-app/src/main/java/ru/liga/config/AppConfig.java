package ru.liga.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.liga.model.ServiceUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class AppConfig {

    private static final AppConfig INSTANCE = new AppConfig();
    private final String propertiesPath = "application.properties";
    private final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private final Properties properties = new Properties();

    public AppConfig() {
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

    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public String getBotToken() {
        return System.getenv("TELEGRAM_BOT_TOKEN");
    }

    public String getBotUserName() {
        return System.getenv("TELEGRAM_BOT_USERNAME");
    }

    public String getLocale() {
        return properties.getProperty("locale");
    }

    public String getTgBotApiUrl() {
        return properties.getProperty("tgbot.api.url");
    }

    public String getProfileUrl() {
        return properties.getProperty("profile.endpoint.url");
    }

    @Bean
    public ServiceUser serviceUser() {
        return new ServiceUser(properties.getProperty("service.user.name"),
                properties.getProperty("service.user.password"));
    }

    public String getRegisterServiceUrl() {
        return properties.getProperty("register.service.url");
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
