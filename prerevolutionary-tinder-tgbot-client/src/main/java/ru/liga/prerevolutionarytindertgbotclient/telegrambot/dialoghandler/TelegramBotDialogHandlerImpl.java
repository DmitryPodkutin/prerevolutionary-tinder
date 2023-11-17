package ru.liga.prerevolutionarytindertgbotclient.telegrambot.dialoghandler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.model.StateType;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.statefactory.StateFactory;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine.BotState;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class TelegramBotDialogHandlerImpl implements TelegramBotDialogHandler {
    private final Map<Long, StateType> usersBotStates = new HashMap<>();
    private final StateFactory stateFactory;

    public void handleUpdate(Update update) {
        final Long chatId = update.getMessage().getChatId();
        StateType currentStateType = usersBotStates.get(chatId);

        if (currentStateType == null) {
            currentStateType = StateType.START;
            usersBotStates.put(chatId, currentStateType);
        }

        // Создаем экземпляр BotState с помощью фабрики состояний
        BotState botState = stateFactory.createState(currentStateType);
        botState.handleInput(this, update);
    }


    @Override
    public void setBotState(Long chatId, StateType newState) {
        usersBotStates.put(chatId, newState);
    }
}
