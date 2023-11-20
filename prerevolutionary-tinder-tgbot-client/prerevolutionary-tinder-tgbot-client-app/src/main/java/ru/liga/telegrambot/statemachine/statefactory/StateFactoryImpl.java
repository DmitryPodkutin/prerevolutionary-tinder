package ru.liga.telegrambot.statemachine.statefactory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.liga.config.AppConfig;
import ru.liga.dto.converter.ProfileEntityToProfileDtoConverter;
import ru.liga.service.ProfileService;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.sender.MessageSender;
import ru.liga.telegrambot.sender.TelegramMessageSender;
import ru.liga.telegrambot.statemachine.BotState;
import ru.liga.telegrambot.statemachine.CreateProfileState;
import ru.liga.telegrambot.statemachine.EditProfileState;
import ru.liga.telegrambot.statemachine.MenuState;
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
    private final ProfileEntityToProfileDtoConverter converter;

    @Override
    public BotState createState(StateType stateType) {
        switch (stateType) {
            case VIEW_PROFILE:
                return new ViewProfileState(resourceBundle, telegramMessageSender);
            case CREATE_PROFILE:
                return new CreateProfileState(restTemplate, messageSender, profileService,
                        resourceBundle, appConfig, converter);
            case EDIT_PROFILE:
                return new EditProfileState(resourceBundle);
            case MENU:
                return new MenuState(resourceBundle);
            default:
                return new StartState(restTemplate, messageSender, appConfig);
        }
    }
}

