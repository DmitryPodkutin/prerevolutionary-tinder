package ru.liga.telegrambot.statemachine.statefactory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.statemachine.BotState;
import ru.liga.telegrambot.statemachine.CreateProfileState;
import ru.liga.telegrambot.statemachine.EditProfileState;
import ru.liga.telegrambot.statemachine.FavoriteState;
import ru.liga.telegrambot.statemachine.MenuState;
import ru.liga.telegrambot.statemachine.RegistrationState;
import ru.liga.telegrambot.statemachine.SearchState;
import ru.liga.telegrambot.statemachine.ViewProfileState;

@Component
@AllArgsConstructor
public class StateFactoryImpl implements StateFactory {
    private final RegistrationState registrationState;
    private final ViewProfileState viewProfileState;
    private final EditProfileState editProfileState;
    private final MenuState menuState;
    private final SearchState searchState;
    private final FavoriteState favoriteState;
    private final CreateProfileState createProfileState;
    @Override
    public BotState createState(StateType stateType) {
        switch (stateType) {
            case REGISTRATION:
                return registrationState;
            case CREATE_PROFILE:
                return createProfileState;
            case EDIT_PROFILE:
                return editProfileState;
            case MENU:
                return menuState;
            case FAVORITES:
                return favoriteState;
            case SEARCH:
                return searchState;
            default:
                return viewProfileState;
        }
    }
}

