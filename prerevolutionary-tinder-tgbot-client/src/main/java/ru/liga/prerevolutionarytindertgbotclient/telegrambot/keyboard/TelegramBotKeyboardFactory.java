package ru.liga.prerevolutionarytindertgbotclient.telegrambot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface TelegramBotKeyboardFactory {
    ReplyKeyboardMarkup createProfileViewKeyboard();
    ReplyKeyboardMarkup createMenuKeyboard();
    ReplyKeyboardMarkup createSwipeKeyboard();
    ReplyKeyboardMarkup createGreetingKeyboard();
    ReplyKeyboardMarkup createLookingForKeyboard();
}
