package ru.liga.telegrambot.sender;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.liga.dto.ProfileDtoWithImage;


/**
 * An interface responsible for sending various messages using Telegram Bot API.
 */
public interface MessageSender {

    /**
     * Sends a text message to a specific chat ID.
     *
     * @param chatId  The chat ID to send the message to.
     * @param message The text message to send.
     */
    void sendTextMessage(Long chatId, String message);

    /**
     * Sends a message with a keyboard attached.
     *
     * @param update   The received update.
     * @param text     The text message to send.
     * @param keyboard The keyboard to attach.
     */
    void sendMessageWithKeyboard(Update update, String text, InlineKeyboardMarkup keyboard);

    /**
     * Opens a keyboard for viewing a profile.
     *
     * @param update             The received update.
     * @param profileDtoWithImage The profile information with an image.
     */
    void openProfileViewKeyboard(Update update, ProfileDtoWithImage profileDtoWithImage);

    /**
     * Opens a menu keyboard.
     *
     * @param update The received update.
     */
    void openMenuKeyboard(Update update);

    /**
     * Opens a keyboard for searching with swipe functionality.
     *
     * @param update  The received update.
     * @param profileDtoWithImage The profile information with an image.
     */
    void openSearchSwipeKeyboard(Update update, ProfileDtoWithImage profileDtoWithImage);

    /**
     * Opens a keyboard for favorites with swipe functionality.
     *
     * @param update     The received update.
     * @param profileDto
     */
    void openFavoritesSwipeKeyboard(Update update, ProfileDtoWithImage profileDto);

    /**
     * Opens a greeting keyboard.
     *
     * @param update The received update.
     */
    void openGreetingKeyboard(Update update);

    /**
     * Opens a keyboard for choosing what to look for.
     *
     * @param update The received update.
     */
    void openLookingForKeyboard(Update update);
}
