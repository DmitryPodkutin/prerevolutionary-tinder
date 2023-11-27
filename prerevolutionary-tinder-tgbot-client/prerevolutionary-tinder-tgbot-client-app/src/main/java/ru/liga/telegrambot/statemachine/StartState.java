package ru.liga.telegrambot.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.config.AppConfig;
import ru.liga.repository.UserStateRepository;
import ru.liga.service.UserService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.sender.MessageSender;

@Slf4j
@Component
public class StartState extends AbstractBotState {

    private final RestTemplate restTemplate;
    private final MessageSender messageSender;
    private final AppConfig appConfig;

    @Autowired
    public StartState(RestTemplate restTemplate,
                      MessageSender messageSender,
                      AppConfig appConfig, UserService userService, UserStateRepository userStateRepository) {
        super(StateType.FAVORITES, userService, userStateRepository);
        this.restTemplate = restTemplate;
        this.messageSender = messageSender;
        this.appConfig = appConfig;
    }


    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        return this;
  /*      final Long chatId = update.getMessage().getChatId();
        final ResponseEntity<String> profileResponse;
        try {

            profileResponse = restTemplate.exchange(appConfig.getProfileUrl(),
                    HttpMethod.GET, null, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Profile not found for URL: {}", appConfig.getProfileUrl());
            return;
        }
        if (profileResponse.getStatusCode().is2xxSuccessful()) {
            final String profileMessage = profileResponse.getBody();
            messageSender.sendMessage(chatId, profileMessage);
            changeUserState(getUserByTelegramId(update), VIEW_PROFILE);
        } else {
            log.error("Profile response not successful. Status code: {}",
                    profileResponse.getStatusCodeValue());
        }*/
    }
}
