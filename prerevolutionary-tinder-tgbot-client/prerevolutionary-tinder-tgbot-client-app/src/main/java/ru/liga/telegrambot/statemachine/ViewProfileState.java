package ru.liga.telegrambot.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.dto.ProfileDtoWithImage;
import ru.liga.integration.service.ProfileClientService;
import ru.liga.model.User;
import ru.liga.repository.UserStateRepository;
import ru.liga.service.UserService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.sender.MessageSender;

import java.util.Objects;
import java.util.ResourceBundle;

import static ru.liga.telegrambot.model.StateType.EDIT_PROFILE;
import static ru.liga.telegrambot.model.StateType.MENU;

@Slf4j
@Component
public class ViewProfileState extends AbstractBotState {
    private final MessageSender messageSender;
    private final ProfileClientService profileClientService;
    private final ResourceBundle logMessages;

    @Autowired
    public ViewProfileState(MessageSender messageSender,
                            UserService userService,
                            UserStateRepository userStateRepository,
                            ProfileClientService profileClientService,
                            ResourceBundle logMessages) {
        super(StateType.FAVORITES, userService, userStateRepository);
        this.messageSender = messageSender;
        this.profileClientService = profileClientService;
        this.logMessages = logMessages;
    }

    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        final String userInput = getUserMessage(update);
        final User user = getUserByTelegramId(update);
        if ("edit.profile.bottom".equals(userInput)) {
            changeUserState(user, EDIT_PROFILE);
            return goToNextStep(EDIT_PROFILE, dialogHandler, update);
        } else if ("menu.bottom".equals(userInput)) {
            changeUserState(user, MENU);
            return goToNextStep(MENU, dialogHandler, update);
        } else {
            getProfile(update);
            return this;
        }
    }

    public void getProfile(Update update) {
        final ResponseEntity<ProfileDtoWithImage> profileResponse = profileClientService.getProfile(
                getUserByTelegramId(update));
        if (profileResponse.getStatusCode().is2xxSuccessful()) {
            final ProfileDtoWithImage profileDtoWithImage = Objects.requireNonNull(profileResponse.getBody());
            messageSender.openProfileViewKeyboard(update, profileDtoWithImage);
        } else {
            log.error(logMessages.getString("error.getting.profile"), profileResponse.getStatusCodeValue());
        }
    }

}
