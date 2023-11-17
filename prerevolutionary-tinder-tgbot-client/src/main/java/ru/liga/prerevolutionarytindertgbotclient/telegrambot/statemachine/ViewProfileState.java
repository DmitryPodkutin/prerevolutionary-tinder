package ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.dialoghandler.TelegramBotDialogHandler;

@Component
public class ViewProfileState implements BotState {
    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        return this;
    }
}
