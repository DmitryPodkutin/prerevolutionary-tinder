package ru.liga.telegrambot.dialoghandler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.liga.service.UserService;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.statemachine.BotState;
import ru.liga.telegrambot.statemachine.statefactory.StateFactory;

import java.util.Optional;

import static java.util.Objects.nonNull;

/**
 * Implementation of the {@link TelegramBotDialogHandler} responsible for handling updates received by the Telegram bot.
 */
@Slf4j
@Component
@AllArgsConstructor
public class TelegramBotDialogHandlerImpl implements TelegramBotDialogHandler {
    private final StateFactory stateFactory;
    private final UserService userService;

    /**
     * Handles the incoming update received by the Telegram bot.
     *
     * @param update The incoming update object.
     */
    @Override
    public void handleUpdate(Update update) {
        log.debug("Received update: {}", update);
        final StateType currentStateType;
        final User telegramUser = getCurrentUser(update);
        final Optional<ru.liga.model.User> userByTelegramId = userService.getUserByTelegramId(telegramUser.getId());
        if (userByTelegramId.isEmpty()) {
            currentStateType = StateType.REGISTRATION;
        } else {
            currentStateType = userByTelegramId.get().getUserState().getStateType();
        }
        final BotState botState = stateFactory.createState(currentStateType);
        botState.handleInput(this, update);
    }

    private User getCurrentUser(Update update) {
        final User currentUser;
        if (nonNull(update.getMessage())) {
            currentUser = update.getMessage().getFrom();
        } else {
            currentUser = update.getCallbackQuery().getFrom();
        }
        return currentUser;
    }
}
