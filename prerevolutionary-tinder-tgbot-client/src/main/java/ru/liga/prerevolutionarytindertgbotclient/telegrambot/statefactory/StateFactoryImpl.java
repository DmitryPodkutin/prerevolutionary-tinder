package ru.liga.prerevolutionarytindertgbotclient.telegrambot.statefactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.model.StateType;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.sender.MessageSender;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.sender.TelegramMessageSender;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine.BotState;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine.EditProfileState;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine.MenuState;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine.StartState;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine.ViewProfileState;

import java.util.ResourceBundle;

@Component
public class StateFactoryImpl implements StateFactory {
    private final RestTemplate restTemplate;
    private final MessageSender messageSender;
    private final String profileEndpointUrl;
    private final ResourceBundle resourceBundle;
    private final TelegramMessageSender telegramMessageSender;

    @Autowired
    public StateFactoryImpl(RestTemplate restTemplate,
                            MessageSender messageSender,
                            @Value("${profile.endpoint.url}") String profileEndpointUrl,
                            ResourceBundle resourceBundle,
                            TelegramMessageSender telegramMessageSender) {
        this.restTemplate = restTemplate;
        this.messageSender = messageSender;
        this.profileEndpointUrl = profileEndpointUrl;
        this.resourceBundle = resourceBundle;
        this.telegramMessageSender = telegramMessageSender;
    }


    @Override
    public BotState createState(StateType stateType) {
        switch (stateType) {
            case VIEW_PROFILE:
                return new ViewProfileState(resourceBundle, telegramMessageSender);
            case EDIT_PROFILE:
                return new EditProfileState(resourceBundle);
            case MENU:
                return new MenuState(resourceBundle);
            default:
                return new StartState(restTemplate, messageSender, profileEndpointUrl);
        }
    }
}

