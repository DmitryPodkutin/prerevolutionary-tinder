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
public class SearchState extends AbstractBotState {
    public SearchState(UserService userService, UserStateRepository userStateRepository,
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
    }

    @Override
    public void handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
    }

}
