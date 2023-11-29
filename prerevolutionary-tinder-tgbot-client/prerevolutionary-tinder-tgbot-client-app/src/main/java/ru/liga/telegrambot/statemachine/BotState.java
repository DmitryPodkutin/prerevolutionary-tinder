package ru.liga.telegrambot.statemachine;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;

/**
 * Represents a state in the Telegram Bot state machine.
 */
public interface BotState {

    /**
     * Handles the input received by the bot according to the current state.
     *
     * @param dialogHandler Handler to manage the bot's dialog flow.
     * @param update        Update object containing the information received from Telegram.
     * @return BotState representing the next state based on the input and current state.
     */
    BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update);

    /**
     * Gets the type of state represented by this BotState instance.
     *
     * @return StateType enum value indicating the type of state.
     */
    StateType getStateType();
}
