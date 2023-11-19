package ru.liga.telegrambot.keyboard;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.liga.exception.KeyboardCreationError;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
@AllArgsConstructor
public class TelegramBotKeyboardFactoryImpl implements TelegramBotKeyboardFactory {
    private static final String MENU_BOTTOM = "menu.bottom";
    private static final String MALE_BOTTOM = "male.bottom";
    private static final String FEMALE_BOTTOM = "female.bottom";
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotKeyboardFactoryImpl.class);
    private final ResourceBundle resourceBundle;

    @Override
    public ReplyKeyboardMarkup createProfileViewKeyboard() {
        final ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        final List<KeyboardRow> keyboardRows = new ArrayList<>();
        try {
            final KeyboardRow row = new KeyboardRow();
            final KeyboardButton editButton = new KeyboardButton(resourceBundle.getString("edit.profile.bottom"));
            final KeyboardButton menuButton = new KeyboardButton(resourceBundle.getString(MENU_BOTTOM));
            row.add(editButton);
            row.add(menuButton);
            keyboardRows.add(row);
        } catch (KeyboardCreationError e) {
            LOGGER.error("Fail to create profile view keyboard.", e);
            throw new KeyboardCreationError("Error while creating profile view keyboard.");
        }

        keyboard.setKeyboard(keyboardRows);
        keyboard.setOneTimeKeyboard(true);
        return keyboard;
    }

    @Override
    public ReplyKeyboardMarkup createMenuKeyboard() {
        final ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        final List<KeyboardRow> keyboardRows = new ArrayList<>();
        try {
            final KeyboardRow row = new KeyboardRow();
            final KeyboardButton searchButton = new KeyboardButton(resourceBundle.getString("search.bottom"));
            final KeyboardButton profileButton = new KeyboardButton(resourceBundle.getString("view.profile.bottom"));
            final KeyboardButton favoriteButton = new KeyboardButton(resourceBundle.getString("favorite.bottom"));
            row.add(searchButton);
            row.add(profileButton);
            row.add(favoriteButton);
            keyboardRows.add(row);
        } catch (KeyboardCreationError e) {
            LOGGER.error("Fail to create menu keyboard.", e);
            throw new KeyboardCreationError("Error while creating menu keyboard.");
        }

        keyboard.setKeyboard(keyboardRows);
        keyboard.setOneTimeKeyboard(true);
        return keyboard;
    }

    @Override
    public ReplyKeyboardMarkup createSwipeKeyboard() {
        final ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        final List<KeyboardRow> keyboardRows = new ArrayList<>();
        try {
            final KeyboardRow row = new KeyboardRow();
            final KeyboardButton leftButton = new KeyboardButton(resourceBundle.getString("left.bottom"));
            final KeyboardButton rightButton = new KeyboardButton(resourceBundle.getString("right.bottom"));
            final KeyboardButton menuButton = new KeyboardButton(resourceBundle.getString(MENU_BOTTOM));
            row.add(leftButton);
            row.add(rightButton);
            row.add(menuButton);
            keyboardRows.add(row);
        } catch (KeyboardCreationError e) {
            LOGGER.error("Fail to create swipe keyboard.", e);
            throw new KeyboardCreationError("Error while creating swipe keyboard.");
        }

        keyboard.setKeyboard(keyboardRows);
        keyboard.setOneTimeKeyboard(true);
        return keyboard;
    }

    @Override
    public ReplyKeyboardMarkup createGreetingKeyboard() {
        final ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        final List<KeyboardRow> keyboardRows = new ArrayList<>();
        try {
            final KeyboardRow row = new KeyboardRow();
            final KeyboardButton maleButton = new KeyboardButton(resourceBundle.getString(MALE_BOTTOM));
            final KeyboardButton femaleButton = new KeyboardButton(resourceBundle.getString(FEMALE_BOTTOM));
            row.add(maleButton);
            row.add(femaleButton);
            keyboardRows.add(row);
        } catch (KeyboardCreationError e) {
            LOGGER.error("Fail to create greeting keyboard.", e);
            throw new KeyboardCreationError("Error while creating greeting keyboard.");
        }
        keyboard.setKeyboard(keyboardRows);
        keyboard.setOneTimeKeyboard(true);
        return keyboard;
    }

    @Override
    public ReplyKeyboardMarkup createLookingForKeyboard() {
        final ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        final List<KeyboardRow> keyboardRows = new ArrayList<>();
        try {
            final KeyboardRow row = new KeyboardRow();
            final KeyboardButton maleButton = new KeyboardButton(resourceBundle.getString(MALE_BOTTOM));
            final KeyboardButton femaleButton = new KeyboardButton(resourceBundle.getString(FEMALE_BOTTOM));
            final KeyboardButton allGenderButton = new KeyboardButton(resourceBundle.getString("all.gender.bottom"));
            row.add(maleButton);
            row.add(femaleButton);
            row.add(allGenderButton);
            keyboardRows.add(row);
        } catch (KeyboardCreationError e) {
            LOGGER.error("Fail to create lookingFor keyboard.", e);
            throw new KeyboardCreationError("Error while creating lookingFor keyboard.");
        }
        keyboard.setKeyboard(keyboardRows);
        keyboard.setOneTimeKeyboard(true);
        return keyboard;
    }






}
