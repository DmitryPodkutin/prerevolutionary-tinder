package ru.liga.telegrambot.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.dto.ProfileDtoWithImage;
import ru.liga.integration.service.ProfileClientServiceImpl;
import ru.liga.model.User;
import ru.liga.repository.UserStateRepository;
import ru.liga.service.UserService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.sender.MessageSender;

import static ru.liga.telegrambot.model.StateType.MENU;
import static ru.liga.telegrambot.model.StateType.SEARCH;

@Slf4j
@Component
public class SearchState extends AbstractBotState {
    private final ProfileClientServiceImpl profileClientService;
    private final MessageSender messageSender;

    @Autowired
    public SearchState(MessageSender messageSender,
                       ProfileClientServiceImpl profileClientService, UserService userService,
                       UserStateRepository userStateRepository) {
        super(SEARCH, userService, userStateRepository);
        this.messageSender = messageSender;
        this.profileClientService = profileClientService;
    }

    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        if (getUserMessage(update).equals("menu.bottom")) {
            changeUserState(getUserByTelegramId(update), MENU);
            return goToNextStep(MENU, dialogHandler, update);
        }
        final Long userTelegramId = getChatId(update);
        final User currentUser = getUserByTelegramId(update);
        final ProfileDtoWithImage profileDto = profileClientService.findNextMatchingProfiles(userTelegramId,
                currentUser).orElseThrow(() -> new RuntimeException(
                String.format("MatchingProfiles fo userTelegramId %s not found ", userTelegramId)));
        messageSender.openSearchSwipeKeyboard(update, profileDto.toString());
        return  this;
    }
}
