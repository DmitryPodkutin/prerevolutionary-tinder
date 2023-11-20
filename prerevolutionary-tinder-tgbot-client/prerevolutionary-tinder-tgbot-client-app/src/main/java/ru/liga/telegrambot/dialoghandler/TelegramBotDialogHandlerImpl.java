package ru.liga.telegrambot.dialoghandler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.statemachine.statefactory.StateFactory;
import ru.liga.telegrambot.statemachine.BotState;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

@Component
@AllArgsConstructor
public class TelegramBotDialogHandlerImpl implements TelegramBotDialogHandler {
    private final Map<Long, StateType> usersBotStates = new HashMap<>();
    private final StateFactory stateFactory;



    public void handleUpdate(Update update) {
        final Long chatId = getChatId(update);
        StateType currentStateType = usersBotStates.get(chatId);

        if (currentStateType == null) {
            currentStateType = StateType.CREATE_PROFILE; //TODO - тут сейчас тестовая логика
            usersBotStates.put(chatId, currentStateType);
        }

        // Создаем экземпляр BotState с помощью фабрики состояний
        final BotState botState = stateFactory.createState(currentStateType);
        botState.handleInput(this, update);
/*        setBotState(chatId, botState.getStateType(), update);*/
    }

    private static Long getChatId(Update update) {
        final Long chatId;
        if (nonNull(update.getMessage())) {
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        return chatId;
    }


    @Override
    public void setBotState(Long chatId, StateType newState, Update update) {
        usersBotStates.put(chatId, newState);
        handleUpdate(update);
    }
}
