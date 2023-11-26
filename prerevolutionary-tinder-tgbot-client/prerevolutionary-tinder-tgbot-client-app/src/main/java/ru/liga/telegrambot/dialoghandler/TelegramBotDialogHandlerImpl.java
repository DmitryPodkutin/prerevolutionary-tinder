package ru.liga.telegrambot.dialoghandler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.liga.service.UserService;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.statemachine.statefactory.StateFactory;
import ru.liga.telegrambot.statemachine.BotState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Component
@AllArgsConstructor
public class TelegramBotDialogHandlerImpl implements TelegramBotDialogHandler {
    private final Map<Long, StateType> usersBotStates = new HashMap<>();
    private final StateFactory stateFactory;
    private final UserService userService;

    public void handleUpdate(Update update) {
        final Long chatId = getChatId(update);
        final StateType currentStateType;
        final User telegramUser = getCurrentUser(update);
        final Optional<ru.liga.model.User> userByTelegramId = userService.getUserByTelegramId(telegramUser.getId());
        if (userByTelegramId.isEmpty()) {
            currentStateType = StateType.REGISTRATION;
        } else {
            currentStateType = userByTelegramId.get().getUserState().getStateType();
        }
        // Создаем экземпляр BotState с помощью фабрики состояний
        final BotState botState = stateFactory.createState(currentStateType);
        botState.handleInput(this, update);
        /*        setBotState(chatId, botState.getStateType(), update);*/
    }
//        if (currentStateType == null) {
//            currentStateType = StateType.CREATE_PROFILE; //TODO - тут сейчас тестовая логика
//            usersBotStates.put(chatId, currentStateType);
//        }




    @Override
    public void setBotState(Long chatId, StateType newState, Update update) {
        usersBotStates.put(chatId, newState);
        handleUpdate(update);
    }

    private Long getChatId(Update update) {
        final Long chatId;
        if (nonNull(update.getMessage())) {
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        return chatId;
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
