package ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine.statefactory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.liga.prerevolutionarytindertgbotclient.config.AppConfig;
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
@AllArgsConstructor
public class StateFactoryImpl implements StateFactory {
    private final RestTemplate restTemplate;
    private final MessageSender messageSender;
    private final AppConfig appConfig;
    private final ResourceBundle resourceBundle;
    private final TelegramMessageSender telegramMessageSender;



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
                return new StartState(restTemplate, messageSender, appConfig);
        }
    }
}

