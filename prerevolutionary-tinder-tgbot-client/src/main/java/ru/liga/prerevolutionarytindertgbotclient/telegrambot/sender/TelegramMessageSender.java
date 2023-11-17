package ru.liga.prerevolutionarytindertgbotclient.telegrambot.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.TinderTelegramBot;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.keyboard.TelegramBotKeyboardFactory;

import java.util.ResourceBundle;

@Component
public class TelegramMessageSender implements MessageSender {

    private  TinderTelegramBot telegramBot;
    private final TelegramBotKeyboardFactory telegramBotKeyboardFactory;
    private final ResourceBundle resourceBundle;

    @Autowired
    public TelegramMessageSender(TelegramBotKeyboardFactory telegramBotKeyboardFactory,
                                 ResourceBundle resourceBundle) {
        this.telegramBotKeyboardFactory = telegramBotKeyboardFactory;
        this.resourceBundle = resourceBundle;
    }

    @Lazy
    @Autowired
    public void setBotMessenger(TinderTelegramBot botMessenger) {
        this.telegramBot = botMessenger;
    }

    @Override
    public void sendMessage(Long chatId, String message) {
        telegramBot.sendMessage(chatId, message);
    }

    @Override
    public void openProfileViewKeyboard(Update update) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(resourceBundle.getString("view.profile.message"));
        sendMessage.setReplyMarkup(telegramBotKeyboardFactory.createProfileViewKeyboard());
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void openMenuKeyboard(Update update) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(resourceBundle.getString("menu.message"));
        sendMessage.setReplyMarkup(telegramBotKeyboardFactory.createMenuKeyboard());
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void openSearchSwipeKeyboard(Update update) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(resourceBundle.getString("search.message"));
        sendMessage.setReplyMarkup(telegramBotKeyboardFactory.createSwipeKeyboard());
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void openFavoritesSwipeKeyboard(Update update) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(resourceBundle.getString("favorite.message"));
        sendMessage.setReplyMarkup(telegramBotKeyboardFactory.createSwipeKeyboard());
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void openGreetingKeyboard(Update update) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(resourceBundle.getString("choose.greeting"));
        sendMessage.setReplyMarkup(telegramBotKeyboardFactory.createSwipeKeyboard());
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void openLookingForKeyboard(Update update) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(resourceBundle.getString("choose.looking.for"));
        sendMessage.setReplyMarkup(telegramBotKeyboardFactory.createSwipeKeyboard());
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
