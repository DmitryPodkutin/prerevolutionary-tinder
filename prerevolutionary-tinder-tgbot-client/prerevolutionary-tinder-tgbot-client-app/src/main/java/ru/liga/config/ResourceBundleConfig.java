package ru.liga.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.ResourceBundle;

@Configuration
public class ResourceBundleConfig {

    @Bean
    public ResourceBundle resourceBundle() {
        return ResourceBundle.getBundle("messages", Locale.getDefault());
    }
}
