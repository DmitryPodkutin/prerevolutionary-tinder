package ru.liga.telegrambot.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.dto.MatchingProfileDtoWithImage;
import ru.liga.integration.service.FavoriteClientService;
import ru.liga.integration.service.ProfileClientServiceImpl;
import ru.liga.model.User;
import ru.liga.repository.UserStateRepository;
import ru.liga.service.UserService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.sender.MessageSender;

import java.util.ResourceBundle;

import static ru.liga.telegrambot.model.StateType.MENU;
import static ru.liga.telegrambot.model.StateType.SEARCH;

/**
 * Represents the state responsible for searching an appropriate profile.
 */
@Slf4j
@Component
public class SearchState extends AbstractBotState {
    private final ProfileClientServiceImpl profileClientService;
    private final FavoriteClientService favoriteClientService;
    private final MessageSender messageSender;
    private final ResourceBundle resourceBundle;

    @Autowired
    public SearchState(MessageSender messageSender,
                       ProfileClientServiceImpl profileClientService, UserService userService,
                       UserStateRepository userStateRepository, FavoriteClientService favoriteClientService,
                       ResourceBundle resourceBundle) {
        super(SEARCH, userService, userStateRepository);
        this.messageSender = messageSender;
        this.profileClientService = profileClientService;
        this.favoriteClientService = favoriteClientService;
        this.resourceBundle = resourceBundle;
    }

    /**
     * Handles the input during the searching process.
     *
     * @param dialogHandler The dialog handler.
     * @param update        The received update.
     * @return The next bot state.
     */
    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        log.debug("Handling input for searching an appropriate profile.");
        if (getUserMessage(update).equals("menu.bottom")) {
            changeUserState(getUserByTelegramId(update), MENU);
            return goToNextStep(MENU, dialogHandler, update);
        }
        final Long userTelegramId = getChatId(update);
        final User currentUser = getUserByTelegramId(update);
        final MatchingProfileDtoWithImage matchingProfileDtoWithImage = profileClientService.
                findNextMatchingProfile(userTelegramId,
                        currentUser).orElseThrow(() -> new RuntimeException(
                        String.format("MatchingProfiles fo userTelegramId %s not found ", userTelegramId)));
        if ("right.bottom".equals(getUserMessage(update))) {
            favoriteClientService.addFavorite(matchingProfileDtoWithImage.getId(), currentUser);
            if (Boolean.TRUE.equals(matchingProfileDtoWithImage.isMutuality())) {
                messageSender.sendTextMessage(getChatId(update),
                        resourceBundle.getString("search.message.mutuality"));
            }
        }
        messageSender.openSearchSwipeKeyboard(update, matchingProfileDtoWithImage);
        return this;
    }
}
