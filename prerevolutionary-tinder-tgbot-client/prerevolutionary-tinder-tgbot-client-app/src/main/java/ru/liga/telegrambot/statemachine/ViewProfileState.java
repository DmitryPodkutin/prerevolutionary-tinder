package ru.liga.telegrambot.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.sender.TelegramMessageSender;

import java.util.ResourceBundle;

@Component

public class ViewProfileState extends AbstractBotState {
    private final ResourceBundle resourceBundle;
    private final TelegramMessageSender telegramMessageSender;

    @Autowired
    public ViewProfileState(ResourceBundle resourceBundle,
                            TelegramMessageSender telegramMessageSender) {
        super(StateType.VIEW_PROFILE);
        this.resourceBundle = resourceBundle;
        this.telegramMessageSender = telegramMessageSender;
    }

    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        final Long chatId = update.getMessage().getChatId();
        final String userInput = update.getMessage().getText();
        telegramMessageSender.openProfileViewKeyboard(update);
        if (resourceBundle.getString("edit.profile.bottom").equals(userInput)) {
            return new EditProfileState(resourceBundle);
            // Замените EditProfileState на ваше фактическое состояние редактирования профиля
        } else if (resourceBundle.getString("menu.bottom").equals(userInput)) {
            return new MenuState(resourceBundle); // Замените MenuState на ваше фактическое состояние меню
        } else {
            // Если пользователь ввел что-то другое, оставляем его в текущем состоянии
            return this;
        }
    }
}
