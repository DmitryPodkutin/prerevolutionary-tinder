package ru.liga.telegrambot.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.model.User;
import ru.liga.repository.UserStateRepository;
import ru.liga.service.UserService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.sender.MessageSender;

import java.util.ResourceBundle;

import static ru.liga.telegrambot.model.StateType.FAVORITES;
import static ru.liga.telegrambot.model.StateType.SEARCH;
import static ru.liga.telegrambot.model.StateType.VIEW_PROFILE;

@Component
public class MenuState extends AbstractBotState {
    private final ResourceBundle resourceBundle;
    private final MessageSender telegramMessageSender;

    @Autowired
    public MenuState(ResourceBundle resourceBundle, UserService userService, UserStateRepository userStateRepository,
                     MessageSender telegramMessageSender) {
        super(StateType.MENU, userService, userStateRepository);
        this.resourceBundle = resourceBundle;
        this.telegramMessageSender = telegramMessageSender;

    }

    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        final String userInput = getUserMessage(update);
        final User user = getUserByTelegramId(update);
        if ("search.bottom".equals(userInput)) {
            changeUserState(user, SEARCH);
            return goToNextStep(SEARCH, dialogHandler, update);
        } else if ("view.profile.bottom".equals(userInput)) {
            changeUserState(user, VIEW_PROFILE);
            return goToNextStep(VIEW_PROFILE, dialogHandler, update);
        } else if ("favorite.bottom".equals(userInput)) {
            changeUserState(user, FAVORITES);
            goToNextStep(FAVORITES, dialogHandler, update);
        }
        telegramMessageSender.openMenuKeyboard(update);
        return this;
    }
}
