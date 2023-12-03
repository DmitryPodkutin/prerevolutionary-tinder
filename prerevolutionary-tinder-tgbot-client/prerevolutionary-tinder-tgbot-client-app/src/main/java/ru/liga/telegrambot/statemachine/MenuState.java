package ru.liga.telegrambot.statemachine;

import lombok.extern.slf4j.Slf4j;
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

/**
 * Represents the state responsible for working with a menu.
 */
@Slf4j
@Component
public class MenuState extends AbstractBotState {
    private final MessageSender telegramMessageSender;
    private final ResourceBundle logMessages;

    @Autowired
    public MenuState(UserService userService,
                     UserStateRepository userStateRepository,
                     MessageSender telegramMessageSender,
                     ResourceBundle logMessages) {
        super(StateType.MENU, userService, userStateRepository);
        this.telegramMessageSender = telegramMessageSender;

        this.logMessages = logMessages;
    }

    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        log.debug(logMessages.getString("handle.menu.input"));
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
            return goToNextStep(FAVORITES, dialogHandler, update);
        }
        telegramMessageSender.openMenuKeyboard(update);
        return this;
    }
}
