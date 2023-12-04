package ru.liga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.dto.converter.ProfileEntityToProfileDtoConverter;
import ru.liga.telegrambot.TinderTelegramBot;

import java.util.HashSet;

@SpringBootApplication
public class PrerevolutionaryTinderTgbotClientApplication {

    public static void main(String[] args) throws TelegramApiException {
        final ConfigurableApplicationContext context =
                SpringApplication.run(PrerevolutionaryTinderTgbotClientApplication.class, args);
        final TinderTelegramBot telegramBot = context.getBean(TinderTelegramBot.class);
        telegramBot.initializeBot();
    }

    @Bean(name = "customConversionService")
    public ConversionService getConversionService(ApplicationContext context) {

        final ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        final HashSet<Converter> converters = new HashSet<>();
        converters.add(context.getBean(ProfileEntityToProfileDtoConverter.class));
        bean.setConverters(converters);
        bean.afterPropertiesSet();
        return bean.getObject();
    }
}


