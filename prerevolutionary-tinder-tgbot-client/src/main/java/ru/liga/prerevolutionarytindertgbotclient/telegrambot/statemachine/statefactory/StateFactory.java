package ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine.statefactory;

import ru.liga.prerevolutionarytindertgbotclient.telegrambot.model.StateType;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine.BotState;

public interface StateFactory {
    BotState createState(StateType stateType);
}
