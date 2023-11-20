package ru.liga.telegrambot.sender;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface MessageSender {
    void sendMessage(Long chatId, String message);
    void sendMessageWithKeyboard(Update update, String text, InlineKeyboardMarkup keyboard);
    void openProfileViewKeyboard(Update update);
    void openMenuKeyboard(Update update);

    void openSearchSwipeKeyboard(Update update);
    void openFavoritesSwipeKeyboard(Update update);
    void openGreetingKeyboard(Update update);
    void openLookingForKeyboard(Update update);
}
