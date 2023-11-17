package ru.liga.prerevolutionarytindertgbotclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.TinderTelegramBot;

@SpringBootApplication
public class PrerevolutionaryTinderTgbotClientApplication {

    public static void main(String[] args) throws TelegramApiException {
        final ConfigurableApplicationContext context =
                SpringApplication.run(PrerevolutionaryTinderTgbotClientApplication.class, args);
        final TinderTelegramBot telegramBot = context.getBean(TinderTelegramBot.class);
        telegramBot.initializeBot();
    }

}
