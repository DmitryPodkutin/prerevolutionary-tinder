package ru.liga.prerevolutionarytindertgbotclient.telegrambot.sender;

public interface MessageSender {
    void sendMessage(Long chatId, String message);
}
