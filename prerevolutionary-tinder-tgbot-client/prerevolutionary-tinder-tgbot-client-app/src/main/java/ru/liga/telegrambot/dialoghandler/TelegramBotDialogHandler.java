package ru.liga.telegrambot.dialoghandler;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Represents an interface for handling incoming updates in the Telegram bot.
 */
public interface TelegramBotDialogHandler {

    /**
     * Handles the incoming update received by the Telegram bot.
     *
     * @param update The incoming update object.
     */
    void handleUpdate(Update update);

}

