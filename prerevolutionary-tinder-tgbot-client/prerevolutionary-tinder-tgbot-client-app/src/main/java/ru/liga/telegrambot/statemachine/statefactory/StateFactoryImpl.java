package ru.liga.telegrambot.statemachine.statefactory;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.liga.config.AppConfig;
import ru.liga.service.ProfileService;
import ru.liga.service.UserService;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.sender.MessageSender;
import ru.liga.telegrambot.sender.TelegramMessageSender;
import ru.liga.telegrambot.statemachine.BotState;
import ru.liga.telegrambot.statemachine.CreateProfileState;
import ru.liga.telegrambot.statemachine.EditProfileState;
import ru.liga.telegrambot.statemachine.MenuState;
import ru.liga.telegrambot.statemachine.RegistrationState;
import ru.liga.telegrambot.statemachine.StartState;
import ru.liga.telegrambot.statemachine.ViewProfileState;

import java.util.ResourceBundle;

@Component
@AllArgsConstructor
public class StateFactoryImpl implements StateFactory {
    private final RestTemplate restTemplate;
    private final MessageSender messageSender;
    private final AppConfig appConfig;
    private final ResourceBundle resourceBundle;
    private final TelegramMessageSender telegramMessageSender;
    private final ProfileService profileService;
    private final ConversionService customConversionService;
    private final RegistrationState registrationState;
    private final ViewProfileState viewProfileState;
    private final UserService userService;

    @Override
    public BotState createState(StateType stateType) {
        switch (stateType) {
            case REGISTRATION:
                return registrationState;
            case VIEW_PROFILE:
                return new ViewProfileState(resourceBundle, telegramMessageSender);
            case CREATE_PROFILE:
                return new CreateProfileState(restTemplate, messageSender, profileService,
                        resourceBundle, appConfig, viewProfileState, customConversionService, userService);
            case EDIT_PROFILE:
                return new EditProfileState(resourceBundle);
            case MENU:
                return new MenuState(resourceBundle);
            default:
                return new StartState(restTemplate, messageSender, appConfig);
        }
    }
}

