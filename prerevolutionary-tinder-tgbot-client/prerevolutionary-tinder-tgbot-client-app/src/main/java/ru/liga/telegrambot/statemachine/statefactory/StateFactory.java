package ru.liga.telegrambot.statemachine.statefactory;

import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.statemachine.BotState;

/**
 * The StateFactory interface represents a factory for creating BotState instances based on their StateType.
 */
public interface StateFactory {
    /**
     * Creates a BotState based on the given StateType.
     *
     * @param stateType The StateType to determine which BotState to create.
     * @return A BotState instance corresponding to the provided StateType.
     */
    BotState createState(StateType stateType);
}
