package ru.liga.prerevolutionarytindertgbotclient.telegrambot.sender;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.liga.prerevolutionarytindertgbotclient.config.AppConfig;
import ru.liga.prerevolutionarytindertgbotclient.telegrambot.keyboard.TelegramBotKeyboardFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

@Component
public class TelegramMessageSender implements MessageSender {

    private static final String API_URL = "https://api.telegram.org/bot";
    private static final String SEND_MESSAGE = "/sendMessage";

    private final TelegramBotKeyboardFactory telegramBotKeyboardFactory;
    private final ResourceBundle resourceBundle;
    private final String botToken;
    private final HttpClient httpClient;

    @Autowired
    public TelegramMessageSender(TelegramBotKeyboardFactory telegramBotKeyboardFactory,
                                 ResourceBundle resourceBundle) {
        this.telegramBotKeyboardFactory = telegramBotKeyboardFactory;
        this.resourceBundle = resourceBundle;
        this.botToken = AppConfig.getInstance().getBotUserName();
        this.httpClient = HttpClients.createDefault();
    }

    public TelegramMessageSender() {
        this.telegramBotKeyboardFactory = null;
        this.resourceBundle = null;
        this.botToken = null;
        this.httpClient = HttpClients.createDefault();
    }

    private HttpPost createHttpPostRequest(String apiUrl, Object body) {
        final HttpPost request = new HttpPost(apiUrl);
        request.addHeader("Content-Type", "application/json");
        final StringEntity params = new StringEntity(body.toString(), StandardCharsets.UTF_8);
        request.setEntity(params);
        return request;
    }

    private void sendHttpRequest(HttpPost request) throws IOException {
        try {
            final HttpResponse response = httpClient.execute(request);
            // Обработка ответа сервера Telegram
            // Например, проверка статуса ответа и т.д.
        } catch (IOException e) {
            throw new IOException("Error while sending message: " + e.getMessage());
        }
    }

    public void sendMessageWithKeyboard(Update update, String text, ReplyKeyboardMarkup keyboard) throws IOException {
        final String apiUrl = API_URL + botToken + SEND_MESSAGE;
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboard);

        final HttpPost request = createHttpPostRequest(apiUrl, sendMessage);
        sendHttpRequest(request);
    }

    public void sendMessage(Long chatId, String message) {
        final String apiUrl = API_URL + botToken + SEND_MESSAGE;

        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);
        try {
            sendHttpRequest(createHttpPostRequest(apiUrl, sendMessage));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void openProfileViewKeyboard(Update update) {
        try {
            sendMessageWithKeyboard(update,
                    resourceBundle.getString("view.profile.message"),
                    telegramBotKeyboardFactory.createProfileViewKeyboard());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void openMenuKeyboard(Update update) {
        try {
            sendMessageWithKeyboard(update, resourceBundle.getString("menu.message"),
                    telegramBotKeyboardFactory.createMenuKeyboard());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void openSearchSwipeKeyboard(Update update) {
        try {
            sendMessageWithKeyboard(update, resourceBundle.getString("search.message"),
                    telegramBotKeyboardFactory.createSwipeKeyboard());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void openFavoritesSwipeKeyboard(Update update) {
        try {
            sendMessageWithKeyboard(update, resourceBundle.getString("favorite.message"),
                    telegramBotKeyboardFactory.createSwipeKeyboard());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void openGreetingKeyboard(Update update) {
        try {
            sendMessageWithKeyboard(update, resourceBundle.getString("choose.greeting"),
                    telegramBotKeyboardFactory.createSwipeKeyboard());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void openLookingForKeyboard(Update update) {
        try {
            sendMessageWithKeyboard(update, resourceBundle.getString("choose.looking.for"),
                    telegramBotKeyboardFactory.createSwipeKeyboard());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
