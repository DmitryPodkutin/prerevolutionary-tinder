package ru.liga.telegrambot.dialoghandler;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.telegrambot.model.StateType;

public interface TelegramBotDialogHandler {
    void handleUpdate(Update update);
    void setBotState(Long chatId, StateType newState, Update update);

}

