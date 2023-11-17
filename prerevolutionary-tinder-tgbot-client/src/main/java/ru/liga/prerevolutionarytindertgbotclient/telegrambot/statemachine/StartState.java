package ru.liga.prerevolutionarytindertgbotclient.telegrambot.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.sender.MessageSender;

import java.io.IOException;

import static ru.liga.prerevolutionarytindertgbotclient.telegrambot.model.StateType.START;
import static ru.liga.prerevolutionarytindertgbotclient.telegrambot.model.StateType.VIEW_PROFILE;

@Component
public class StartState extends AbstractBotState {
    private final RestTemplate restTemplate;
    private final MessageSender messageSender;
    private String profileEndpointUrl;


    @Autowired
    public StartState(RestTemplate restTemplate,
                      MessageSender messageSender,
                      @Value("${profile.endpoint.url}") String profileEndpointUrl) {
        super(START);
        this.restTemplate = restTemplate;
        this.messageSender = messageSender;
        this.profileEndpointUrl = profileEndpointUrl;
    }


    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        final Long chatId = update.getMessage().getChatId();
        final ResponseEntity<String> profileResponse;

        try {
            profileResponse = restTemplate.exchange(profileEndpointUrl, HttpMethod.GET, null, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            return this;
        }

        if (profileResponse.getStatusCode().is2xxSuccessful()) {
            // Получаем сообщение с профилем
            final String profileMessage = profileResponse.toString();

            // Отправляем сообщение с профилем пользователю
            try {
                messageSender.sendMessage(chatId, profileMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Переходим в состояние просмотра профиля
            dialogHandler.setBotState(chatId, VIEW_PROFILE, update);
        } else {
            return this;
        }
        return this;
    }
}
