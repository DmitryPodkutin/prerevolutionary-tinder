package ru.liga.prerevolutionarytindertgbotclient.telegrambot.dialoghandler;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.model.StateType;

public interface TelegramBotDialogHandler {
    void handleUpdate(Update update);
    void setBotState(Long chatId, StateType newState, Update update);

}

