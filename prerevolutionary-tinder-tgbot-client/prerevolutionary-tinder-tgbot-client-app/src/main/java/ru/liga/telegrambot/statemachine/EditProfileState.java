package ru.liga.telegrambot.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.repository.UserStateRepository;
import ru.liga.service.UserService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;

import java.util.ResourceBundle;

@Component
public class EditProfileState extends AbstractBotState {
    private final ResourceBundle resourceBundle;

    @Autowired
    public EditProfileState(ResourceBundle resourceBundle, UserService userService,
                            UserStateRepository userStateRepository) {
        super(StateType.EDIT_PROFILE, userService, userStateRepository);
        this.resourceBundle = resourceBundle;
    }

    @Override
    public void handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        return;
    }
}
