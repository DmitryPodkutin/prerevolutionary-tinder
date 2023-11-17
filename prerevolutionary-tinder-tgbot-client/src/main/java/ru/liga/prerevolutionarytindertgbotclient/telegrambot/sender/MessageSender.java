package ru.liga.prerevolutionarytindertgbotclient.telegrambot.sender;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageSender {
    void sendMessage(Long chatId, String message);
    void openProfileViewKeyboard(Update update);
}
