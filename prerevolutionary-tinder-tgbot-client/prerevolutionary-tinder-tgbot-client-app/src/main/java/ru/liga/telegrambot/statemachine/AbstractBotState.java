package ru.liga.telegrambot.statemachine;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;

import static java.util.Objects.nonNull;


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
    public Long getChatId(Update update) {
        final Long chatId;
        if (nonNull(update.getMessage())) {
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        return chatId;
    }

//    protected void authenticateAndSetBotState(TelegramBotDialogHandler dialogHandler,
//                                              Update update) {
//        final User telegramUser = update.getMessage().getFrom();
//        final Long chatId = update.getMessage().getChatId();
//        final Optional<AuthorizedUser> present = authService.authenticateUser(telegramUser.getId());
//        if (present.isPresent()) {
//            dialogHandler.setBotState(chatId, StateType.START, update);
//        } else {
//            dialogHandler.setBotState(chatId, REGISTRATION, update);
//        }
//    }
}
