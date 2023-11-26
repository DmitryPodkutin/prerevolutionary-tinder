package ru.liga.telegrambot.dialoghandler;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramBotDialogHandler {

    void handleUpdate(Update update);

}

