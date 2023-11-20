package ru.liga.telegrambot.statemachine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.config.AppConfig;
import ru.liga.exception.CreatingProfileError;
import ru.liga.model.Profile;
import ru.liga.service.ProfileService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.sender.MessageSender;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.liga.telegrambot.model.StateType.CREATE_PROFILE;
import static ru.liga.telegrambot.model.StateType.VIEW_PROFILE;

@Component
public class CreateProfileState extends AbstractBotState {
    private static final String MALE_BOTTOM = "male.bottom";
    private static final String FEMALE_BOTTOM = "female.bottom";
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateProfileState.class);
    public static final String EMPTY_STRING = "";


    private final RestTemplate restTemplate;
    private final MessageSender messageSender;
    private final ProfileService profileService;
    private final ResourceBundle resourceBundle;
    private final AppConfig appConfig;

    @Autowired
    public CreateProfileState(RestTemplate restTemplate,
                              MessageSender messageSender,
                              ProfileService profileService, ResourceBundle resourceBundle,
                              AppConfig appConfig) {
        super(CREATE_PROFILE);
        this.restTemplate = restTemplate;
        this.messageSender = messageSender;
        this.profileService = profileService;
        this.resourceBundle = resourceBundle;
        this.appConfig = appConfig;
    }

    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        final Long chatId = getChatId(update);
        final Optional<Profile> profileOptional = profileService.getByChatId(chatId);
        if (profileOptional.isEmpty()) {
            if (!getProfileGender(update).isEmpty()) {
                return this;
            }
        }

        final Profile profile = profileService.getByChatId(chatId).orElseThrow(EntityNotFoundException::new);

        if (isNull(profile.getName()) || profile.getName().equals(EMPTY_STRING)) {
            if (!getProfileName(update, profile).isEmpty()) {
                return this;
            }
        }
        if (isNull(profile.getDescription()) || profile.getDescription().equals(EMPTY_STRING)) {
            if (!getProfileDescription(update, profile).isEmpty()) {
                return this;
            }
        }
        if (isNull(profile.getLookingFor()) || profile.getLookingFor().equals(EMPTY_STRING)) {
            if (!getProfileLookingFor(update, profile).isEmpty()) {
                return this;
            }
        }
        createProfile(dialogHandler, update, chatId, profile);
        return this;
    }

    @Nullable
    private Optional<CreateProfileState> getProfileLookingFor(Update update, Profile profile) {
        if (isNull(profile.getLookingFor())) {
            profile.setLookingFor(EMPTY_STRING);
            profileService.saveProfile(profile);
            messageSender.openLookingForKeyboard(update);
            return Optional.of(this);
        } else {
            fillLookingFor(profile, update.getCallbackQuery().getData());
            return Optional.empty();
        }
    }

    @NotNull
    private Optional<CreateProfileState> getProfileDescription(Update update, Profile profile) {
        if (isNull(profile.getDescription())) {
            profile.setDescription(EMPTY_STRING);
            profileService.saveProfile(profile);
            messageSender.sendMessage(update.getMessage().getChatId(),
                    resourceBundle.getString("choose.description"));
            return Optional.of(this);
        } else {
            fillDescription(profile, update.getMessage().getText());
            profileService.saveProfile(profile);
            return Optional.empty();
        }
    }

    @NotNull
    private Optional<CreateProfileState> getProfileName(Update update, Profile profile) {
        if (isNull(profile.getName())) {
            profile.setName(EMPTY_STRING);
            profileService.saveProfile(profile);
            messageSender.sendMessage(getChatId(update),
                    resourceBundle.getString("choose.name"));
            return Optional.of(this);
        } else {
            profile.setName(update.getMessage().getText());
            profileService.saveProfile(profile);
            return Optional.empty();
        }
    }

    @NotNull
    private Optional<CreateProfileState> getProfileGender(Update update) {
        if (nonNull(update.getCallbackQuery()) && nonNull(update.getCallbackQuery().getChatInstance())) {
            final Profile profile = new Profile();
            profile.setChatId(getChatId(update));
            fillGender(profile, update.getCallbackQuery().getData());
            profileService.saveProfile(profile);
            return Optional.empty();
        } else {
            messageSender.openGreetingKeyboard(update);
            return Optional.of(this);
        }
    }

    private void createProfile(TelegramBotDialogHandler dialogHandler, Update update, Long chatId, Profile profile) {
        try {
            final ResponseEntity<Void> response = restTemplate.exchange(
                    appConfig.getProfileUrl(),
                    HttpMethod.POST,
                    new HttpEntity<>(profile), // Передача profileDTO в качестве тела запроса
                    Void.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                dialogHandler.setBotState(chatId, VIEW_PROFILE, update); // Установка следующего состояния
            } else {
                LOGGER.error("Error creating profile. Status code: {}", response.getStatusCodeValue());
            }
        } catch (Exception e) {
            LOGGER.error("Error while creating profile: {}", e.getMessage());
        }
    }

    private Profile fillGender(Profile profile, String messageText) {
        if (messageText.equals(MALE_BOTTOM) ||
                messageText.equals(FEMALE_BOTTOM)) {
            profile.setGender(resourceBundle.getString(messageText));
            profileService.saveProfile(profile);
            return profile;
        } else {
            throw new CreatingProfileError("Wrong gender.");
        }
    }

    private Profile fillLookingFor(Profile profile, String messageText) {
        if (messageText.equals(MALE_BOTTOM) ||
                messageText.equals(FEMALE_BOTTOM) ||
                messageText.equals("all.gender.bottom")) {
            profile.setLookingFor(messageText);
            return profile;
        } else {
            throw new CreatingProfileError("Wrong choice of looking for.");
        }
    }

    private Profile fillDescription(Profile profile, String messageText) {
        final int newlineIndex = messageText.indexOf("\n");
        final int endIndex = (newlineIndex != -1) ? newlineIndex : messageText.length();

        final String descriptionHeader = messageText.substring(0, endIndex);
        final String description = (endIndex < messageText.length()) ? messageText.substring(endIndex + 1) : EMPTY_STRING;
        profile.setDescriptionHeader(descriptionHeader);
        profile.setDescription(description);
        return profile;
    }

    private static Long getChatId(Update update) {
        final Long chatId;
        if (nonNull(update.getMessage())) {
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        return chatId;
    }
}
