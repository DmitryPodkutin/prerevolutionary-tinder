package ru.liga.telegrambot.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.model.UserState;
import ru.liga.repository.UserStateRepository;
import ru.liga.service.UserService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;

import javax.persistence.EntityNotFoundException;

import static java.util.Objects.nonNull;


/**
 * Abstract class defining the common functionalities and state handling methods for the Telegram bot.
 */
public abstract class AbstractBotState implements BotState {

    private final StateType stateType;
    private final UserService userService;
    private final UserStateRepository userStateRepository;
    private  MenuState menuState;
    private  ViewProfileState viewProfileState;
    private  EditProfileState editProfileState;
    private  SearchState searchState;
    private  FavoriteState favoriteState;
    private  CreateProfileState createProfileState;


    public AbstractBotState(StateType stateType, UserService userService, UserStateRepository
            userStateRepository) {
        this.stateType = stateType;
        this.userService = userService;
        this.userStateRepository = userStateRepository;
    }

    @Autowired
    @Lazy
    public void setMenuState(MenuState menuState) {
        this.menuState = menuState;
    }

    @Autowired
    @Lazy
    public void setViewProfileState(ViewProfileState viewProfileState) {
        this.viewProfileState = viewProfileState;
    }


    @Autowired
    @Lazy
    public void setEditProfileState(EditProfileState editProfileState) {
        this.editProfileState = editProfileState;
    }


    @Autowired
    @Lazy
    public void setSearchState(SearchState searchState) {
        this.searchState = searchState;
    }


    @Autowired
    @Lazy
    public void setFavoriteState(FavoriteState favoriteState) {
        this.favoriteState = favoriteState;
    }


    @Autowired
    @Lazy
    public void setCreateProfileState(CreateProfileState createProfileState) {
        this.createProfileState = createProfileState;
    }

    @Override
    public StateType getStateType() {
        return stateType;
    }

    /**
     * Handles the input to the bot state.
     *
     * @param dialogHandler The dialog handler.
     * @param update        The received update.
     * @return The next bot state to transition to.
     */
    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        return menuState;
    }

    /**
     * Returns the chat ID from the received update.
     *
     * @param update The received update.
     * @return The chat ID.
     */
    public Long getChatId(Update update) {
        final Long chatId;
        if (nonNull(update.getMessage())) {
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        return chatId;
    }

    /**
     * Changes the user's state to the specified state type.
     *
     * @param user           The user whose state is to be changed.
     * @param stateTypeToSet The state type to set for the user.
     */
    public void changeUserState(ru.liga.model.User user, StateType stateTypeToSet) {
        final UserState userState = userStateRepository.findByUserId(user.getId())
                .orElseThrow(EntityNotFoundException::new);
        userState.setStateType(stateTypeToSet);
        userStateRepository.save(userState);
    }

    /**
     * Retrieves the user based on the Telegram ID from the received update.
     *
     * @param update The received update.
     * @return The user.
     */
    public ru.liga.model.User getUserByTelegramId(Update update) {
        final Long telegramId;
        if (nonNull(update.getMessage())) {
            telegramId = update.getMessage().getFrom().getId();
        } else {
            telegramId = update.getCallbackQuery().getFrom().getId();
        }
        return userService.getUserByTelegramId(telegramId)
                .orElseThrow(EntityNotFoundException::new);
    }

    /**
     * Retrieves the message from the received update.
     *
     * @param update The received update.
     * @return The user message.
     */
    public String getUserMessage(Update update) {
        if (nonNull(update.getCallbackQuery())) {
            return update.getCallbackQuery().getData();
        } else {
            return update.getMessage().getText();
        }
    }

    /**
     * Moves the bot to the next step/state based on the specified state type.
     *
     * @param stateTypeToGo            The state type to move to.
     * @param telegramBotDialogHandler The dialog handler.
     * @param update                   The received update.
     * @return The next bot state.
     */
    public BotState goToNextStep(StateType stateTypeToGo, TelegramBotDialogHandler
            telegramBotDialogHandler, Update update) {
        final StateType stateTypeSwitch = stateTypeToGo;
        switch (stateTypeSwitch) {
            case VIEW_PROFILE:
                return viewProfileState.handleInput(telegramBotDialogHandler, update);
            case EDIT_PROFILE:
                return editProfileState.handleInput(telegramBotDialogHandler, update);
            case CREATE_PROFILE:
                return createProfileState.handleInput(telegramBotDialogHandler, update);
            case SEARCH:
                return searchState.handleInput(telegramBotDialogHandler, update);
            case FAVORITES:
                return favoriteState.handleInput(telegramBotDialogHandler, update);
            default:
                return menuState.handleInput(telegramBotDialogHandler, update);
        }
    }
}

