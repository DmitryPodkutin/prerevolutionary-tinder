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

import java.util.Optional;
import java.util.ResourceBundle;

import static java.util.Objects.isNull;
import static ru.liga.telegrambot.model.StateType.CREATE_PROFILE;
import static ru.liga.telegrambot.model.StateType.VIEW_PROFILE;

/**
 * Represents the state responsible for editing a user profile.
 */
@Slf4j
@Component
public class EditProfileState extends AbstractBotState {
    private static final String MALE_BOTTOM = "male.bottom";
    private static final String FEMALE_BOTTOM = "female.bottom";
    private static final String EMPTY_STRING = "";
    private static final String ALL_GENDER_BOTTOM = "all.gender.bottom";

    private final MessageSender messageSender;
    private final ProfileService profileService;
    private final ResourceBundle resourceBundle;
    private final ConversionService customConversionService;
    private final ProfileClientService profileClientService;

    @Autowired
    public EditProfileState(MessageSender messageSender,
                            ProfileService profileService, ResourceBundle resourceBundle,
                            ConversionService customConversionService, ProfileClientService profileClientService,
                            UserService userService, UserStateRepository userStateRepository) {
        super(CREATE_PROFILE, userService, userStateRepository);
        this.messageSender = messageSender;
        this.profileService = profileService;
        this.resourceBundle = resourceBundle;
        this.customConversionService = customConversionService;
        this.profileClientService = profileClientService;
    }

    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        log.debug("Handling input for editing a user profile.");
        final Long chatId = getChatId(update);
        final Optional<Profile> profileOptional = profileService.getByChatId(chatId);

        if (getUserMessage(update).equals("edit.profile.bottom")) {
            messageSender.openGreetingKeyboard(update);
            return this;
        }

        final Profile profile = createProfileWithGender(profileOptional, update);


        if (isProfileNameEmpty(update, profile)) {
            return this;
        }

        if (isProfileDescriptionEmpty(update, profile)) {
            return this;
        }

        if (isProfileLookingForEmpty(update, profile)) {
            return this;
        }

        final User user = getUserByTelegramId(update);
        updateProfileAndChangeState(profile, user);
        return goToNextStep(VIEW_PROFILE, dialogHandler, update);
    }

    private Profile createProfileWithGender(Optional<Profile> profileOptional, Update update) {
        log.debug("Checking profile gender information.");
        if (profileOptional.isEmpty()) {
            final Profile profile = new Profile();
            profile.setChatId(getChatId(update));
            fillProfileGender(profile, update.getCallbackQuery().getData());
            profileService.saveProfile(profile);
            return profile;
        } else {
            return profileOptional.get();
        }
    }

    private boolean isProfileNameEmpty(Update update, Profile profile) {
        log.debug("Checking profile name information.");
        if (isNull(profile.getName()) || profile.getName().equals(EMPTY_STRING)) {
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
        } else {
            return false;
        }
    }

    private boolean isProfileDescriptionEmpty(Update update, Profile profile) {
        log.debug("Checking profile description information.");
        if (isNull(profile.getDescription()) || profile.getDescription().equals(EMPTY_STRING)) {
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
        } else {
            return false;
        }
    }

    private boolean isProfileLookingForEmpty(Update update, Profile profile) {
        log.debug("Checking profile looking for information.");
        if (isNull(profile.getLookingFor()) || profile.getLookingFor().equals(EMPTY_STRING)) {
            if (isNull(profile.getLookingFor())) {
                profile.setLookingFor(EMPTY_STRING);
                profileService.saveProfile(profile);
                messageSender.openLookingForKeyboard(update);
                return true;
            } else {
                fillLookingFor(profile, update.getCallbackQuery().getData());
                return false;
            }
        } else {
            return false;
        }
    }

    private void updateProfileAndChangeState(Profile profile, User user) {
        updateProfile(profile, user);
        changeUserState(user, VIEW_PROFILE);
        profileService.deleteTempProfile(profile);
    }

    private void fillProfileGender(Profile profile, String messageText) {
        if (messageText.equals(MALE_BOTTOM) ||
                messageText.equals(FEMALE_BOTTOM)) {
            profile.setGender(resourceBundle.getString(messageText));
            profileService.saveProfile(profile);
        } else {
            throw new CreatingProfileError("Wrong gender.");
        }
    }

    private void fillLookingFor(Profile profile, String messageText) {
        if (messageText.equals(MALE_BOTTOM) ||
                messageText.equals(FEMALE_BOTTOM) ||
                messageText.equals(ALL_GENDER_BOTTOM)) {
            profile.setLookingFor(resourceBundle.getString(messageText));
            profileService.saveProfile(profile);
        } else {
            throw new CreatingProfileError("Wrong choice of looking for.");
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

    private void updateProfile(Profile profile, User user) {
        profileClientService.updateProfile(customConversionService.convert(profile, ProfileDto.class),
                user);
        profileService.deleteTempProfile(profile);
    }
}
