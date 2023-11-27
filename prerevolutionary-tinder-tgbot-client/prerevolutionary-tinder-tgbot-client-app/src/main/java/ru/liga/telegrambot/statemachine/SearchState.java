package ru.liga.telegrambot.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.integration.service.ProfileClientServiceImpl;
import ru.liga.model.User;
import ru.liga.repository.UserStateRepository;
import ru.liga.service.UserService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.sender.MessageSender;

import java.util.Optional;

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
    public void handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        final Long userTelegramId = update.getMessage().getFrom().getId();
        final User currentUser = getUserByTelegramId(update);
        messageSender.sendMessage(update.getMessage().getChatId(),
                profileClientService.findNextMatchingProfiles(userTelegramId, currentUser)
                        .get().toString());
    }


}
