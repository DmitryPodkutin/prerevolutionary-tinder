package ru.liga.telegrambot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface TelegramBotKeyboardFactory {
    InlineKeyboardMarkup createProfileViewKeyboard();

    InlineKeyboardMarkup createMenuKeyboard();

    InlineKeyboardMarkup createSwipeKeyboard();

    InlineKeyboardMarkup createGreetingKeyboard();

    InlineKeyboardMarkup createLookingForKeyboard();
}
