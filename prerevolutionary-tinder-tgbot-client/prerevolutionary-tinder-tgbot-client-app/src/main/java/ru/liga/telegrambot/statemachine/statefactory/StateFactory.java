package ru.liga.telegrambot.statemachine.statefactory;

import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.statemachine.BotState;

public interface StateFactory {
    BotState createState(StateType stateType);
}
