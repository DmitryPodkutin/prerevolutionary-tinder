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

    @Override
    public void handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        return;
    }

    public Long getChatId(Update update) {
        final Long chatId;
        if (nonNull(update.getMessage())) {
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        return chatId;
    }

    public void changeUserState(ru.liga.model.User user, StateType stateTypeToSet) {
        final UserState userState = userStateRepository.findByUserId(user.getId())
                .orElseThrow(EntityNotFoundException::new);
        userState.setStateType(stateTypeToSet);
        userStateRepository.save(userState);
    }

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

    public String getUserMessage(Update update) {
        if (nonNull(update.getCallbackQuery())) {
            return update.getCallbackQuery().getData();
        } else {
            return update.getMessage().getText();
        }
    }

    public void goToNextStep(StateType stateTypeToGo, TelegramBotDialogHandler
            telegramBotDialogHandler, Update update) {
        switch (stateTypeToGo) {
            case MENU:
                menuState.handleInput(telegramBotDialogHandler, update);
            case VIEW_PROFILE:
                viewProfileState.handleInput(telegramBotDialogHandler, update);
            case EDIT_PROFILE:
                editProfileState.handleInput(telegramBotDialogHandler, update);
            case CREATE_PROFILE:
                createProfileState.handleInput(telegramBotDialogHandler, update);
            case SEARCH:
                searchState.handleInput(telegramBotDialogHandler, update);
            case FAVORITES:
                favoriteState.handleInput(telegramBotDialogHandler, update);
            default:
                viewProfileState.handleInput(telegramBotDialogHandler, update);
        }
    }
}

