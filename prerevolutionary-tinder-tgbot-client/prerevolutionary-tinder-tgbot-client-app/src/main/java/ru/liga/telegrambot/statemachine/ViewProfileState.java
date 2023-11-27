package ru.liga.telegrambot.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.dto.ProfileDto;
import ru.liga.integration.service.ProfileClientService;
import ru.liga.model.User;
import ru.liga.repository.UserStateRepository;
import ru.liga.service.UserService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.sender.MessageSender;
import ru.liga.telegrambot.sender.TelegramMessageSender;

import java.util.Objects;
import java.util.ResourceBundle;

import static ru.liga.telegrambot.model.StateType.EDIT_PROFILE;
import static ru.liga.telegrambot.model.StateType.MENU;

@Slf4j
@Component
public class ViewProfileState extends AbstractBotState {
    private final ResourceBundle resourceBundle;
    private final TelegramMessageSender telegramMessageSender;
    private final MessageSender messageSender;
    private final ProfileClientService profileClientService;

    @Autowired
    public ViewProfileState(ResourceBundle resourceBundle, TelegramMessageSender telegramMessageSender,
                            MessageSender messageSender, UserService userService,
                            UserStateRepository userStateRepository, ProfileClientService profileClientService) {
        super(StateType.FAVORITES, userService, userStateRepository);
        this.resourceBundle = resourceBundle;
        this.telegramMessageSender = telegramMessageSender;
        this.messageSender = messageSender;
        this.profileClientService = profileClientService;
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
        final ResponseEntity<ProfileDto> profileResponse = profileClientService.getProfile(
                getUserByTelegramId(update));
        if (profileResponse.getStatusCode().is2xxSuccessful()) {
            final String profileMessage = Objects.requireNonNull(profileResponse.getBody()).toString();
            messageSender.openProfileViewKeyboard(update, profileMessage);
        } else {
            log.error("Profile response not successful. Status code: {}", profileResponse.getStatusCodeValue());
        }
    }

}
