package ru.liga.telegrambot.statemachine.statefactory;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.ResourceBundle;

/**
 * A factory responsible for creating different states of the Telegram Bot based on the StateType.
 */
@Slf4j
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
    private final ResourceBundle logMessages;

    /**
     * Creates and returns the corresponding BotState based on the provided StateType.
     *
     * @param stateType The StateType indicating the type of state to be created.
     * @return BotState representing the specific state based on the StateType.
     */
    @Override
    public BotState createState(StateType stateType) {
        switch (stateType) {
            case REGISTRATION:
                log.debug(logMessages.getString("debug.create.registration.state"));
                return registrationState;
            case CREATE_PROFILE:
                log.debug(logMessages.getString("debug.create.create.profile.state"));
                return createProfileState;
            case EDIT_PROFILE:
                log.debug(logMessages.getString("debug.create.edit.profile.state"));
                return editProfileState;
            case MENU:
                log.debug(logMessages.getString("debug.create.menu.state"));
                return menuState;
            case FAVORITES:
                log.debug(logMessages.getString("debug.create.favorites.state"));
                return favoriteState;
            case SEARCH:
                log.debug(logMessages.getString("debug.create.search.state"));
                return searchState;
            default:
                log.debug(logMessages.getString("debug.create.view.profile.state"));
                return viewProfileState;
        }
    }
}

