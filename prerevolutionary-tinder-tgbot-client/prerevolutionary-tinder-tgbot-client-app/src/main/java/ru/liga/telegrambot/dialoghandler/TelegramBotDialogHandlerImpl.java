package ru.liga.telegrambot.dialoghandler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.liga.service.UserService;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.statemachine.BotState;
import ru.liga.telegrambot.statemachine.statefactory.StateFactory;

import java.util.Optional;

import static java.util.Objects.nonNull;

@Component
@AllArgsConstructor
public class TelegramBotDialogHandlerImpl implements TelegramBotDialogHandler {
    private final StateFactory stateFactory;
    private final UserService userService;

    @Override
    public void handleUpdate(Update update) {
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
