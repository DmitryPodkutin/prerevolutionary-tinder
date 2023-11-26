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
                      AppConfig appConfig, UserService userService, UserStateRepository userStateRepository,
                      MenuState menuState, EditProfileState editProfileState,
                      ViewProfileState viewProfileState, SearchState searchState, FavoriteState favoriteState,
                      CreateProfileState createProfileState) {
        super(StateType.FAVORITES, userService, userStateRepository,
                menuState,
                viewProfileState,
                editProfileState,
                searchState,
                favoriteState,
                createProfileState);
        this.restTemplate = restTemplate;
        this.messageSender = messageSender;
        this.appConfig = appConfig;
    }


    @Override
    public void handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
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
