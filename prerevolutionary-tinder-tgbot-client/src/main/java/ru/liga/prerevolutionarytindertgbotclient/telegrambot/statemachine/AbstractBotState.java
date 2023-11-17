package ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.model.StateType;


public abstract class AbstractBotState implements BotState {

    private final StateType stateType;

    public AbstractBotState(StateType stateType) {
        this.stateType = stateType;
    }

    @Override
    public StateType getStateType() {
        return stateType;
    }

    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        return null;
    }
}
