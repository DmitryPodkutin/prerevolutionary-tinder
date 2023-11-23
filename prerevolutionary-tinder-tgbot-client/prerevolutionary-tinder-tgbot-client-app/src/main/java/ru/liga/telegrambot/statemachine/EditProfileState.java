package ru.liga.telegrambot.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;

import java.util.ResourceBundle;

@Component
public class EditProfileState extends AbstractBotState {
    private final ResourceBundle resourceBundle;

    @Autowired
    public EditProfileState(ResourceBundle resourceBundle) {
        super(StateType.EDIT_PROFILE);
        this.resourceBundle = resourceBundle;
    }

    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        return null;
    }
}
