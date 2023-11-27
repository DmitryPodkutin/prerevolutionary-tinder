package ru.liga.telegrambot.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.repository.UserStateRepository;
import ru.liga.service.UserService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;

@Slf4j
@Component
public class FavoriteState extends AbstractBotState {
    public FavoriteState(UserService userService, UserStateRepository userStateRepository) {
        super(StateType.FAVORITES, userService, userStateRepository);
    }

    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        return this;
    }
}
