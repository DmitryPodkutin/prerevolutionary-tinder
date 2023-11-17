package ru.liga.prerevolutionarytindertgbotclient.telegrambot.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.BotMessenger;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.TinderTelegramBot;

@Component
public class TelegramMessageSender implements MessageSender {

    private  BotMessenger telegramBot;

    @Lazy
    @Autowired
    public void setBotMessenger(TinderTelegramBot botMessenger) {
        this.telegramBot = botMessenger;
    }

    @Override
    public void sendMessage(Long chatId, String message) {
        telegramBot.sendMessage(chatId, message);
    }
}
