package ru.liga.telegrambot.statemachine;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;

public interface BotState {
    void handleInput(TelegramBotDialogHandler dialogHandler, Update update);

    StateType getStateType();
}
