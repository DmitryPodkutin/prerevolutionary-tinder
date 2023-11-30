package ru.liga.telegrambot.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.dto.ProfileDtoWithImage;
import ru.liga.emuns.SwipeDirection;
import ru.liga.integration.service.ProfileClientServiceImpl;
import ru.liga.model.User;
import ru.liga.repository.UserStateRepository;
import ru.liga.service.UserService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.sender.MessageSender;

import static ru.liga.telegrambot.model.StateType.MENU;

@Slf4j
@Component
public class FavoriteState extends AbstractBotState {

    private final ProfileClientServiceImpl profileClientService;
    private final MessageSender messageSender;
    public FavoriteState(UserService userService, UserStateRepository userStateRepository,
                         ProfileClientServiceImpl profileClientService, MessageSender messageSender) {
        super(StateType.FAVORITES, userService, userStateRepository);
        this.profileClientService = profileClientService;
        this.messageSender = messageSender;
    }

    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        log.debug("Handling input for favorites.");
        if (getUserMessage(update).equals("menu.bottom")) {
            changeUserState(getUserByTelegramId(update), MENU);
            return goToNextStep(MENU, dialogHandler, update);
        }
        final Long userTelegramId = getChatId(update);
        final User currentUser = getUserByTelegramId(update);
        final ProfileDtoWithImage profileDto = profileClientService.findNextFavoriteProfile(userTelegramId,
                currentUser, setDirection(update)).orElseThrow(() -> new RuntimeException(
                String.format("MatchingProfiles fo userTelegramId %s not found ", userTelegramId)));
        messageSender.openFavoritesSwipeKeyboard(update, profileDto);
        return  this;
    }

    private SwipeDirection setDirection(Update update) {
        if (getUserMessage(update).equals("left.bottom")) {
            return SwipeDirection.BACKWARD;
        } else {
            return SwipeDirection.FORWARD;
        }
    }
}
