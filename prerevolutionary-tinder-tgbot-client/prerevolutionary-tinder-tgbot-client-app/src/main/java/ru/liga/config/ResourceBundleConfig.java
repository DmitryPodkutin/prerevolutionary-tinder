package ru.liga.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Configuration class for loading resource bundles.
 */
@Configuration
public class ResourceBundleConfig {

    /**
     * Provides a resource bundle for general messages.
     *
     * @return ResourceBundle for general messages.
     */
    @Bean
    public ResourceBundle resourceBundle() {
        return ResourceBundle.getBundle("messages", Locale.getDefault());
    }

    /**
     * Provides a resource bundle for log messages.
     *
     * @return ResourceBundle for log messages.
     */
    @Bean
    public ResourceBundle logMessages() {
        return ResourceBundle.getBundle("log_message", Locale.getDefault());
    }
}
