package ru.liga.telegrambot;

public interface BotMessenger {
    void sendMessage(Long chatId, String text);
}
