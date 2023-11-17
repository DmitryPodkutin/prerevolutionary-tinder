package ru.liga.prerevolutionarytindertgbotclient.telegrambot;

public interface BotMessenger {
    void sendMessage(Long chatId, String text);
}
