package ru.liga.telegrambot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * Interface responsible for creating different types of inline keyboards for the Telegram bot.
 */
public interface TelegramBotKeyboardFactory {

    /**
     * Creates an inline keyboard for viewing a user profile.
     *
     * @return The InlineKeyboardMarkup representing the profile view keyboard.
     */
    InlineKeyboardMarkup createProfileViewKeyboard();

    /**
     * Creates a menu inline keyboard.
     *
     * @return The InlineKeyboardMarkup representing the menu keyboard.
     */
    InlineKeyboardMarkup createMenuKeyboard();


    /**
     * Creates an inline keyboard for swiping actions.
     *
     * @return The InlineKeyboardMarkup representing the swipe keyboard.
     */
    InlineKeyboardMarkup createSwipeKeyboard();

    /**
     * Creates a greeting inline keyboard.
     *
     * @return The InlineKeyboardMarkup representing the greeting keyboard.
     */
    InlineKeyboardMarkup createGreetingKeyboard();

    /**
     * Creates an inline keyboard for selecting preferences or looking for options.
     *
     * @return The InlineKeyboardMarkup representing the keyboard for preferences or looking for options.
     */
    InlineKeyboardMarkup createLookingForKeyboard();
}
