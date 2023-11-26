package ru.liga.telegrambot.statemachine;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.config.AppConfig;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.sender.MessageSender;

import static ru.liga.telegrambot.model.StateType.START;
import static ru.liga.telegrambot.model.StateType.VIEW_PROFILE;

@Slf4j
@Component
public class StartState extends AbstractBotState {

    private final RestTemplate restTemplate;
    private final MessageSender messageSender;
    private final AppConfig appConfig;

    @Autowired
    public StartState(RestTemplate restTemplate,
                      MessageSender messageSender,
                      AppConfig appConfig) {
        super(START);
        this.restTemplate = restTemplate;
        this.messageSender = messageSender;
        this.appConfig = appConfig;
    }


    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        final Long chatId = update.getMessage().getChatId();
        final ResponseEntity<String> profileResponse;
        try {

            profileResponse = restTemplate.exchange(appConfig.getProfileUrl(),
                    HttpMethod.GET, null, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Profile not found for URL: {}", appConfig.getProfileUrl());
            return this;
        }
        if (profileResponse.getStatusCode().is2xxSuccessful()) {
            final String profileMessage = profileResponse.getBody();
            messageSender.sendMessage(chatId, profileMessage);
            dialogHandler.setBotState(chatId, VIEW_PROFILE, update);
        } else {
            log.error("Profile response not successful. Status code: {}",
                    profileResponse.getStatusCodeValue());
            return this;
        }
        return this;
    }
}
