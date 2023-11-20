package ru.liga.telegrambot;

public interface BotMessenger {
    void sendMessageWithKeyboard(Long chatId, String text);
}
