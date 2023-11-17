package ru.liga.prerevolutionarytindertgbotclient.telegrambot.keyboard;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
@AllArgsConstructor
public class TelegramBotKeyboardFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotKeyboardFactory.class);
    private final ResourceBundle resourceBundle;

    public ReplyKeyboardMarkup createProfileViewKeyboard() {
        final ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        final List<KeyboardRow> keyboardRows = new ArrayList<>();
        try {
            final KeyboardRow row = new KeyboardRow();
            final KeyboardButton editButton = new KeyboardButton(resourceBundle.getString("edit.profile.bottom"));
            final KeyboardButton menuButton = new KeyboardButton(resourceBundle.getString("menu.bottom"));
            row.add(editButton);
            row.add(menuButton);
            keyboardRows.add(row);
        } catch (Exception e) {
            LOGGER.error("Error while creating profile view keyboard.", e);
        }

        keyboard.setKeyboard(keyboardRows);
        keyboard.setOneTimeKeyboard(true);
        return keyboard;
    }

}
