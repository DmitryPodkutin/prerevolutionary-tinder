package ru.liga.telegrambot.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.config.AppConfig;
import ru.liga.model.User;
import ru.liga.model.UserState;
import ru.liga.repository.UserStateRepository;
import ru.liga.service.UserService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.sender.MessageSender;
import ru.liga.telegrambot.sender.TelegramMessageSender;

import javax.persistence.EntityNotFoundException;
import java.util.ResourceBundle;

import static java.util.Objects.nonNull;
import static ru.liga.telegrambot.model.StateType.EDIT_PROFILE;
import static ru.liga.telegrambot.model.StateType.MENU;

@Component

public class ViewProfileState extends AbstractBotState {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewProfileState.class);
    private final ResourceBundle resourceBundle;
    private final TelegramMessageSender telegramMessageSender;
    private final RestTemplate restTemplate;
    private final AppConfig appConfig;
    private final MessageSender messageSender;
    private final UserService userService;
    private final UserStateRepository userStateRepository;

    @Autowired
    public ViewProfileState(ResourceBundle resourceBundle,
                            TelegramMessageSender telegramMessageSender, RestTemplate restTemplate,
                            AppConfig appConfig, MessageSender messageSender, UserService userService,
                            UserStateRepository userStateRepository) {
        super(StateType.VIEW_PROFILE);
        this.resourceBundle = resourceBundle;
        this.telegramMessageSender = telegramMessageSender;
        this.restTemplate = restTemplate;
        this.appConfig = appConfig;
        this.messageSender = messageSender;
        this.userService = userService;
        this.userStateRepository = userStateRepository;
    }

    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        final String userInput;
        if (nonNull(update.getCallbackQuery())) {
            userInput = update.getCallbackQuery().getMessage().getText();
        } else {
            userInput = update.getMessage().getText();
        }
        final User user = userService.getUserByTelegramId(getCurrentUser(update).getId())
                .orElseThrow(EntityNotFoundException::new);
        if (resourceBundle.getString("edit.profile.bottom").equals(userInput)) {
            final UserState userState = userStateRepository.findByUserId(user.getId())
                    .orElseThrow(EntityNotFoundException::new);
            userState.setStateType(EDIT_PROFILE);
            userStateRepository.save(userState);
            return new EditProfileState(resourceBundle);
        } else if (resourceBundle.getString("menu.bottom").equals(userInput)) {
            final UserState userState = userStateRepository.findByUserId(user.getId())
                    .orElseThrow(EntityNotFoundException::new);
            userState.setStateType(MENU);
            userStateRepository.save(userState);
            return new MenuState(resourceBundle); // Замените MenuState на ваше фактическое состояние меню
        } else {
            getProfile(update);
            telegramMessageSender.openProfileViewKeyboard(update);
            return this;
        }
    }

    public void getProfile(Update update) {
        ResponseEntity<String> profileResponse = null;
        try {
            final User user = userService.getUserByTelegramId(update.getCallbackQuery().getFrom().getId())
                    .orElseThrow(EntityNotFoundException::new);
            final HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(user.getUserName(), user.getPassword());
            headers.setContentType(MediaType.APPLICATION_JSON);
            profileResponse = restTemplate.getForEntity(appConfig.getProfileUrl(), null, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            LOGGER.error("Profile not found for URL: {}", appConfig.getProfileUrl());
        }
        if (profileResponse.getStatusCode().is2xxSuccessful()) {
            final String profileMessage = profileResponse.getBody();
            messageSender.sendMessage(getChatId(update), profileMessage);
        } else {
            LOGGER.error("Profile response not successful. Status code: {}",
                    profileResponse.getStatusCodeValue());
        }
    }

}
