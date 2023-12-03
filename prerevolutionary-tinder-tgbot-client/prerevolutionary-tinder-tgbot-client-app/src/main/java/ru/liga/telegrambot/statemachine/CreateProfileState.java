package ru.liga.telegrambot.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.dto.ProfileDto;
import ru.liga.exception.CreatingProfileError;
import ru.liga.integration.service.ProfileClientService;
import ru.liga.model.Profile;
import ru.liga.model.User;
import ru.liga.repository.UserStateRepository;
import ru.liga.service.ProfileService;
import ru.liga.service.UserService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.sender.MessageSender;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.liga.telegrambot.model.StateType.CREATE_PROFILE;
import static ru.liga.telegrambot.model.StateType.VIEW_PROFILE;

/**
 * Represents the state responsible for creating a user profile.
 */
@Slf4j
@Component
public class CreateProfileState extends AbstractBotState {
    private static final String MALE_BOTTOM = "male.bottom";
    private static final String FEMALE_BOTTOM = "female.bottom";
    private static final String EMPTY_STRING = "";
    private static final String ALL_GENDER_BOTTOM = "all.gender.bottom";

    private final MessageSender messageSender;
    private final ProfileService profileService;
    private final ResourceBundle resourceBundle;
    private final ConversionService customConversionService;
    private final ProfileClientService profileClientService;
    private final ResourceBundle logMessages;

    @Autowired
    public CreateProfileState(MessageSender messageSender,
                              ProfileService profileService,
                              ResourceBundle resourceBundle,
                              ConversionService customConversionService,
                              ProfileClientService profileClientService,
                              UserService userService,
                              UserStateRepository userStateRepository, ResourceBundle logMessages) {
        super(CREATE_PROFILE, userService, userStateRepository);
        this.messageSender = messageSender;
        this.profileService = profileService;
        this.resourceBundle = resourceBundle;
        this.customConversionService = customConversionService;
        this.profileClientService = profileClientService;
        this.logMessages = logMessages;
    }

    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        log.debug(logMessages.getString("handle.create.profile.input"));
        final Long chatId = getChatId(update);
        final Optional<Profile> profileOptional = profileService.getByChatId(chatId);
        if (profileOptional.isEmpty()) {
            if (Boolean.TRUE.equals(getProfileGender(update))) {
                return this;
            }
        }

        final Profile profile = profileService.getByChatId(chatId).orElseThrow(EntityNotFoundException::new);

        if (isNull(profile.getName()) || profile.getName().equals(EMPTY_STRING)) {
            if (Boolean.TRUE.equals(getProfileName(update, profile))) {
                return this;
            }
        }
        if (isNull(profile.getDescription()) || profile.getDescription().equals(EMPTY_STRING)) {
            if (Boolean.TRUE.equals(getProfileDescription(update, profile))) {
                return this;
            }
        }
        if (isNull(profile.getLookingFor()) || profile.getLookingFor().equals(EMPTY_STRING)) {
            if (Boolean.TRUE.equals(getProfileLookingFor(update, profile))) {
                return this;
            }
        }

        final User user = getUserByTelegramId(update);
        profileClientService.createProfile(customConversionService.convert(profile, ProfileDto.class),
                user);
        profileService.deleteTempProfile(profile);
        changeUserState(user, VIEW_PROFILE);
        return goToNextStep(VIEW_PROFILE, dialogHandler, update);
    }

    private Boolean getProfileLookingFor(Update update, Profile profile) {
        log.debug(logMessages.getString("check.looking.for"));
        if (isNull(profile.getLookingFor())) {
            profile.setLookingFor(EMPTY_STRING);
            profileService.saveProfile(profile);
            messageSender.openLookingForKeyboard(update);
            return true;
        } else {
            fillLookingFor(profile, update.getCallbackQuery().getData());
            return false;
        }
    }

    private Boolean getProfileDescription(Update update, Profile profile) {
        log.debug(logMessages.getString("check.description"));
        if (isNull(profile.getDescription())) {
            profile.setDescription(EMPTY_STRING);
            profileService.saveProfile(profile);
            messageSender.sendTextMessage(update.getMessage().getChatId(),
                    resourceBundle.getString("choose.description"));
            return true;
        } else {
            fillDescription(profile, update.getMessage().getText());
            profileService.saveProfile(profile);
            return false;
        }
    }

    private Boolean getProfileName(Update update, Profile profile) {
        log.debug(logMessages.getString("check.name"));
        if (isNull(profile.getName())) {
            profile.setName(EMPTY_STRING);
            profileService.saveProfile(profile);
            messageSender.sendTextMessage(getChatId(update),
                    resourceBundle.getString("choose.name"));
            return true;
        } else {
            profile.setName(update.getMessage().getText());
            profileService.saveProfile(profile);
            return false;
        }
    }

    private Boolean getProfileGender(Update update) {
        log.debug(logMessages.getString("check.gender"));
        if (nonNull(update.getCallbackQuery()) && nonNull(update.getCallbackQuery().getChatInstance())) {
            final Profile profile = new Profile();
            profile.setChatId(getChatId(update));
            fillGender(profile, update.getCallbackQuery().getData());
            profileService.saveProfile(profile);
            return false;
        } else {
            messageSender.openGreetingKeyboard(update);
            return true;
        }
    }

    private void fillGender(Profile profile, String messageText) {
        if (messageText.equals(MALE_BOTTOM) ||
                messageText.equals(FEMALE_BOTTOM)) {
            profile.setGender(resourceBundle.getString(messageText));
            profileService.saveProfile(profile);
        } else {
            final String errorMessage = logMessages.getString("wrong.gender");
            log.error(errorMessage);
            throw new CreatingProfileError(errorMessage);
        }
    }

    private void fillLookingFor(Profile profile, String messageText) {
        if (messageText.equals(MALE_BOTTOM) ||
                messageText.equals(FEMALE_BOTTOM) ||
                messageText.equals(ALL_GENDER_BOTTOM)) {
            profile.setLookingFor(resourceBundle.getString(messageText));
        } else {
            final String errorMessage = logMessages.getString("wrong.looking.for");
            log.error(errorMessage);
            throw new CreatingProfileError(errorMessage);
        }
    }

    private void fillDescription(Profile profile, String messageText) {
        final int newlineIndex = messageText.indexOf("\n");
        final int endIndex = (newlineIndex != -1) ? newlineIndex : messageText.length();

        final String descriptionHeader = messageText.substring(0, endIndex);
        final String description = (endIndex < messageText.length()) ?
                messageText.substring(endIndex + 1) : EMPTY_STRING;
        profile.setDescriptionHeader(descriptionHeader);
        profile.setDescription(description);
    }
}
