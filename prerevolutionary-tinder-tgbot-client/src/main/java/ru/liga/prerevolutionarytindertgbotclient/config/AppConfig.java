package ru.liga.prerevolutionarytindertgbotclient.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {

    private static final AppConfig INSTANCE = new AppConfig();
    private final String propertiesPath =
            "/home/kbashkatova/Документы/prerevolutionary-tinder/prerevolutionary-tinder-tgbot-client/" +
                    "src/main/resources/application.properties";


    private final Logger logger = LoggerFactory.getLogger(AppConfig.class);


    private final Properties properties = new Properties();

    private AppConfig() {
        try (FileInputStream input = new FileInputStream(propertiesPath)) {
            properties.load(input);
        } catch (IOException e) {
            logger.error("Failed to load application properties from " + propertiesPath, e);
            throw new RuntimeException(propertiesPath, e);
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
}
