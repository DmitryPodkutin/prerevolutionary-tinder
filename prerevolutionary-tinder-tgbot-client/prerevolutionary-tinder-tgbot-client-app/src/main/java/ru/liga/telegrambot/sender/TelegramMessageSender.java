package ru.liga.telegrambot.sender;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.liga.config.AppConfig;
import ru.liga.dto.ProfileDtoWithImage;
import ru.liga.telegrambot.keyboard.TelegramBotKeyboardFactory;
import ru.liga.telegrambot.sender.imagesender.ImageSender;
import ru.liga.telegrambot.sender.textsender.TextSender;

import java.util.ResourceBundle;

import static java.util.Objects.nonNull;

/**
 * A component responsible for sending various types of messages using Telegram Bot API.
 */
@Slf4j
@Component
@AllArgsConstructor
public class TelegramMessageSender implements MessageSender {

    private static final String SEND_MESSAGE_ENDPOINT = "/sendMessage";
    private static final String SEND_PHOTO_ENDPOINT = "/sendPhoto";
    private static final String CHAT_ID_ENDPOINT = "?chat_id=";
    private final TelegramBotKeyboardFactory telegramBotKeyboardFactory;
    private final ResourceBundle resourceBundle;
    private final AppConfig appConfig;
    private final ImageSender imageSender;
    private final TextSender textSender;


    /**
     * Opens the keyboard for viewing a profile.
     *
     * @param update             The received update.
     * @param profileDtoWithImage The profile information with an image.
     */
    @Override
    public void openProfileViewKeyboard(Update update, ProfileDtoWithImage profileDtoWithImage) {
        final String profileMessage = formatOutputProfileMessage(profileDtoWithImage);
        sendMessageWithPhotoAndKeyboard(update, profileDtoWithImage.getImage(), profileMessage,
                telegramBotKeyboardFactory.createProfileViewKeyboard());
    }

    @Override
    public void openMenuKeyboard(Update update) {
        sendMessageWithKeyboard(update, resourceBundle.getString("menu.message"),
                telegramBotKeyboardFactory.createMenuKeyboard());
    }

    @Override
    public void openSearchSwipeKeyboard(Update update, ProfileDtoWithImage profileDtoWithImage) {
        final String profileMessage = formatOutputProfileMessage(profileDtoWithImage);
        sendMessageWithPhotoAndKeyboard(update, profileDtoWithImage.getImage(), profileMessage,
                telegramBotKeyboardFactory.createSwipeKeyboard());
    }

    @Override
    public void openFavoritesSwipeKeyboard(Update update) {
        sendMessageWithKeyboard(update, resourceBundle.getString("favorite.message"),
                telegramBotKeyboardFactory.createSwipeKeyboard());

    }

    @Override
    public void openGreetingKeyboard(Update update) {
        sendMessageWithKeyboard(update, resourceBundle.getString("choose.greeting"),
                telegramBotKeyboardFactory.createGreetingKeyboard());
    }

    @Override
    public void openLookingForKeyboard(Update update) {
        sendMessageWithKeyboard(update, resourceBundle.getString("choose.looking.for"),
                telegramBotKeyboardFactory.createLookingForKeyboard());
    }

    public void sendMessageWithKeyboard(Update update, String text, InlineKeyboardMarkup keyboard) {
        final String apiUrl = appConfig.getTgBotApiUrl() + appConfig.getBotToken() +
                SEND_MESSAGE_ENDPOINT + CHAT_ID_ENDPOINT + getChatId(update).toString();
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(getChatId(update).toString());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboard);
        textSender.sendHttpRequest(apiUrl, sendMessage);
    }

    public void sendTextMessage(Long chatId, String message) {
        final String apiUrl = appConfig.getTgBotApiUrl() + appConfig.getBotToken() +
                SEND_MESSAGE_ENDPOINT + CHAT_ID_ENDPOINT + chatId.toString();
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);
        textSender.sendHttpRequest(apiUrl, sendMessage);
    }

    public void sendMessageWithPhotoAndKeyboard(Update update, byte[] image,
                                                String text, InlineKeyboardMarkup keyboard) {
        final String messageApiUrl = appConfig.getTgBotApiUrl() + appConfig.getBotToken() +
                SEND_MESSAGE_ENDPOINT + CHAT_ID_ENDPOINT + getChatId(update).toString();
        final String photoApiUrl = appConfig.getTgBotApiUrl() + appConfig.getBotToken() +
                SEND_PHOTO_ENDPOINT;
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(getChatId(update).toString());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboard);

        imageSender.sendHttpRequest(image, photoApiUrl, update);
        textSender.sendHttpRequest(messageApiUrl, sendMessage);
    }

    private Long getChatId(Update update) {
        if (nonNull(update.getCallbackQuery())) {
            return update.getCallbackQuery().getMessage().getChatId();
        } else {
            return update.getMessage().getChatId();
        }
    }

    private String formatOutputProfileMessage(ProfileDtoWithImage profileDtoWithImage) {
        return String.format(profileDtoWithImage.getGender() + ", " + profileDtoWithImage.getName());
    }
}
