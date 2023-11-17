package ru.liga.prerevolutionarytindertgbotclient.telegrambot.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.TinderTelegramBot;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.keyboard.TelegramBotKeyboardFactory;

@Component
public class TelegramMessageSender implements MessageSender {

    private  TinderTelegramBot telegramBot;
    private TelegramBotKeyboardFactory telegramBotKeyboardFactory;

    public TelegramMessageSender(TelegramBotKeyboardFactory telegramBotKeyboardFactory) {
        this.telegramBotKeyboardFactory = telegramBotKeyboardFactory;
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
        sendMessage.setText("Ваш профиль");

        sendMessage.setReplyMarkup(telegramBotKeyboardFactory.createProfileViewKeyboard());

        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
