package ru.liga.telegrambot.keyboard;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.liga.exception.KeyboardCreationError;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Slf4j
@Component
@AllArgsConstructor
public class TelegramBotKeyboardFactoryImpl implements TelegramBotKeyboardFactory {

    private static final String MENU_BOTTOM = "menu.bottom";
    private static final String MALE_BOTTOM = "male.bottom";
    private static final String FEMALE_BOTTOM = "female.bottom";
    private static final String LEFT_BOTTOM = "left.bottom";
    private static final String RIGHT_BOTTOM = "right.bottom";
    private static final String ALL_GENDER_BOTTOM = "all.gender.bottom";
    private static final String SEARCH_BOTTOM = "search.bottom";
    private static final String VIEW_PROFILE_BOTTOM = "view.profile.bottom";
    private static final String FAVORITE_BOTTOM = "favorite.bottom";

    private static final String EDIT_PROFILE_BOTTOM = "edit.profile.bottom";

    private final ResourceBundle resourceBundle;
    private final ResourceBundle logMessages;

    /**
     * Creates an inline keyboard for viewing a user profile.
     *
     * @return The InlineKeyboardMarkup representing the profile view keyboard.
     */
    @Override
    public InlineKeyboardMarkup createProfileViewKeyboard() {
        final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        final InlineKeyboardButton editButton = new InlineKeyboardButton();
        editButton.setText(resourceBundle.getString(EDIT_PROFILE_BOTTOM));
        editButton.setCallbackData(EDIT_PROFILE_BOTTOM);

        final InlineKeyboardButton menuButton = new InlineKeyboardButton();
        menuButton.setText(resourceBundle.getString(MENU_BOTTOM));
        menuButton.setCallbackData(MENU_BOTTOM);

        final List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(editButton);
        row.add(menuButton);
        keyboardRows.add(row);

        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }

    @Override
    public InlineKeyboardMarkup createMenuKeyboard() {
        final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        final  List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        final InlineKeyboardButton searchButton = new InlineKeyboardButton();
        searchButton.setText(resourceBundle.getString(SEARCH_BOTTOM));
        searchButton.setCallbackData(SEARCH_BOTTOM);

        final InlineKeyboardButton profileButton = new InlineKeyboardButton();
        profileButton.setText(resourceBundle.getString(VIEW_PROFILE_BOTTOM));
        profileButton.setCallbackData(VIEW_PROFILE_BOTTOM);

        final InlineKeyboardButton favoriteButton = new InlineKeyboardButton();
        favoriteButton.setText(resourceBundle.getString(FAVORITE_BOTTOM));
        favoriteButton.setCallbackData(FAVORITE_BOTTOM);

        final List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(searchButton);
        row.add(profileButton);
        row.add(favoriteButton);
        keyboardRows.add(row);

        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }

    /**
     * Creates an inline keyboard for swiping actions.
     *
     * @return The InlineKeyboardMarkup representing the swipe keyboard.
     * @throws KeyboardCreationError if there is an error while creating the swipe inline keyboard.
     */
    @Override
    public InlineKeyboardMarkup createSwipeKeyboard() {
        final InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        try {
            final List<InlineKeyboardButton> row = new ArrayList<>();
            final InlineKeyboardButton leftButton = new InlineKeyboardButton();
            leftButton.setText(resourceBundle.getString(LEFT_BOTTOM));
            leftButton.setCallbackData(LEFT_BOTTOM);
            final InlineKeyboardButton rightButton = new InlineKeyboardButton();
            rightButton.setText(resourceBundle.getString(RIGHT_BOTTOM));
            rightButton.setCallbackData(RIGHT_BOTTOM);
            final InlineKeyboardButton menuButton = new InlineKeyboardButton();
            menuButton.setText(resourceBundle.getString(MENU_BOTTOM));
            menuButton.setCallbackData(MENU_BOTTOM);
            row.add(leftButton);
            row.add(rightButton);
            row.add(menuButton);
            keyboard.add(row);
        } catch (KeyboardCreationError e) {
            log.error(logMessages.getString("keyboard.creation.error.swipe"), e);
            throw new KeyboardCreationError("Error while creating swipe inline keyboard.");
        }

        inlineKeyboard.setKeyboard(keyboard);
        return inlineKeyboard;
    }

    @Override
    public InlineKeyboardMarkup createGreetingKeyboard() {
        final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        final InlineKeyboardButton maleButton = new InlineKeyboardButton();
        maleButton.setText(resourceBundle.getString(MALE_BOTTOM));
        maleButton.setCallbackData(MALE_BOTTOM);

        final InlineKeyboardButton femaleButton = new InlineKeyboardButton();
        femaleButton.setText(resourceBundle.getString(FEMALE_BOTTOM));
        femaleButton.setCallbackData(FEMALE_BOTTOM);

        final List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(maleButton);
        row.add(femaleButton);
        keyboardRows.add(row);

        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }


    @Override
    public InlineKeyboardMarkup createLookingForKeyboard() {
        final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        final InlineKeyboardButton maleButton = new InlineKeyboardButton();
        maleButton.setText(resourceBundle.getString(MALE_BOTTOM));
        maleButton.setCallbackData(MALE_BOTTOM);

        final InlineKeyboardButton femaleButton = new InlineKeyboardButton();
        femaleButton.setText(resourceBundle.getString(FEMALE_BOTTOM));
        femaleButton.setCallbackData(FEMALE_BOTTOM);

        final InlineKeyboardButton allGenderButton = new InlineKeyboardButton();
        allGenderButton.setText(resourceBundle.getString(ALL_GENDER_BOTTOM));
        allGenderButton.setCallbackData(ALL_GENDER_BOTTOM);

        final List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(maleButton);
        row.add(femaleButton);
        row.add(allGenderButton);
        keyboardRows.add(row);

        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }






}
