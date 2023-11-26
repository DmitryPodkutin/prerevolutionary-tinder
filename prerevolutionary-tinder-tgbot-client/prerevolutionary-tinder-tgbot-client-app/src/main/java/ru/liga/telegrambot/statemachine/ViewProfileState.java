package ru.liga.telegrambot.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.integration.service.ProfileClientService;
import ru.liga.model.User;
import ru.liga.repository.UserStateRepository;
import ru.liga.service.UserService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.sender.MessageSender;
import ru.liga.telegrambot.sender.TelegramMessageSender;

import java.util.ResourceBundle;

import static ru.liga.telegrambot.model.StateType.EDIT_PROFILE;
import static ru.liga.telegrambot.model.StateType.MENU;

@Slf4j
@Component
public class ViewProfileState extends AbstractBotState {
    private final ResourceBundle resourceBundle;
    private final TelegramMessageSender telegramMessageSender;
    private final MessageSender messageSender;
    private final EditProfileState editProfileState;
    private final MenuState menuState;
    private final ProfileClientService profileClientService;

    @Autowired
    public ViewProfileState(ResourceBundle resourceBundle, TelegramMessageSender telegramMessageSender,
                            MessageSender messageSender, UserService userService,
                            UserStateRepository userStateRepository, ProfileClientService profileClientService,
                            MenuState menuState, EditProfileState editProfileState, ViewProfileState viewProfileState,
                            SearchState searchState, FavoriteState favoriteState,
                            CreateProfileState createProfileState) {
        super(StateType.FAVORITES, userService, userStateRepository, menuState, viewProfileState,
                editProfileState, searchState, favoriteState, createProfileState);
        this.resourceBundle = resourceBundle;
        this.telegramMessageSender = telegramMessageSender;
        this.messageSender = messageSender;
        this.editProfileState = editProfileState;
        this.menuState = menuState;
        this.profileClientService = profileClientService;
    }

    @Override
    public void handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        final String userInput = getUserMessage(update);
        final User user = getUserByTelegramId(update);
        if (resourceBundle.getString("edit.profile.bottom").equals(userInput)) {
            changeUserState(user, EDIT_PROFILE);
            goToNextStep(EDIT_PROFILE, dialogHandler, update);
        } else if (resourceBundle.getString("menu.bottom").equals(userInput)) {
            changeUserState(user, MENU);
            goToNextStep(MENU, dialogHandler, update);
        } else {
            getProfile(update);
            telegramMessageSender.openProfileViewKeyboard(update);
        }
    }

    public void getProfile(Update update) {
        final ResponseEntity<String> profileResponse = profileClientService.getProfile(getUserByTelegramId(update));
        if (profileResponse.getStatusCode().is2xxSuccessful()) {
            final String profileMessage = profileResponse.getBody();
            messageSender.sendMessage(getChatId(update), profileMessage);
        } else {
            log.error("Profile response not successful. Status code: {}", profileResponse.getStatusCodeValue());
        }
    }

}
