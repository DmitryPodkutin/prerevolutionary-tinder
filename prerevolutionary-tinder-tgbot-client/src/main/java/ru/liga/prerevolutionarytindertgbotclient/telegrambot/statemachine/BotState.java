package ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.model.StateType;

public interface BotState {
    BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update);

    StateType getStateType();
}
