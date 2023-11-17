package ru.liga.prerevolutionarytindertgbotclient.telegrambot.statefactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.model.StateType;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.sender.MessageSender;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine.BotState;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine.StartState;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine.ViewProfileState;

@Component
public class StateFactoryImpl implements StateFactory {
    private final RestTemplate restTemplate;
    private final MessageSender messageSender;
    private final String profileEndpointUrl;

    @Autowired
    public StateFactoryImpl(RestTemplate restTemplate,
                        MessageSender messageSender,
                        @Value("${profile.endpoint.url}") String profileEndpointUrl) {
        this.restTemplate = restTemplate;
        this.messageSender = messageSender;
        this.profileEndpointUrl = profileEndpointUrl;
    }
    @Override
    public BotState createState(StateType stateType) {
        switch (stateType) {
            case VIEW_PROFILE:
                return new ViewProfileState();
            default:
                return new StartState(restTemplate, messageSender, profileEndpointUrl);
        }
    }
}
