package ru.liga.telegrambot.sender;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.liga.config.AppConfig;
import ru.liga.telegrambot.keyboard.TelegramBotKeyboardFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

@Component
public class TelegramMessageSender implements MessageSender {

    private static final String SEND_MESSAGE_ENDPOINT = "/sendMessage";
    private static final String SEND_PHOTO_ENDPOINT = "/sendPhoto";
    private static final String CHAT_ID_ENDPOINT = "?chat_id=";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private final Logger logger = LoggerFactory.getLogger(TelegramMessageSender.class);
    private final TelegramBotKeyboardFactory telegramBotKeyboardFactory;
    private final ResourceBundle resourceBundle;
    private final String botToken;
    private final HttpClient httpClient;
    private final AppConfig appConfig;

    @Autowired
    public TelegramMessageSender(TelegramBotKeyboardFactory telegramBotKeyboardFactory,
                                 ResourceBundle resourceBundle, AppConfig appConfig) {
        this.telegramBotKeyboardFactory = telegramBotKeyboardFactory;
        this.resourceBundle = resourceBundle;
        this.appConfig = appConfig;
        this.botToken = AppConfig.getInstance().getBotToken();
        this.httpClient = HttpClients.createDefault();
    }

    public TelegramMessageSender() {
        this.telegramBotKeyboardFactory = null;
        this.resourceBundle = null;
        this.botToken = null;
        this.appConfig = null;
        this.httpClient = HttpClients.createDefault();
    }

    private HttpPost createHttpPostRequest(String apiUrl, Object body) {
        final HttpPost request = new HttpPost(apiUrl);
        request.addHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON);
        if (body != null) {
            final Gson gson = new Gson();
            final String jsonBody = gson.toJson(body);
            final StringEntity params = new StringEntity(jsonBody, StandardCharsets.UTF_8);
            request.setEntity(params);
        }

        return request;
    }

    private void sendHttpRequest(HttpPost request) throws IOException {
        try {
            final HttpResponse response = httpClient.execute(request);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.valueOf(statusCode).is2xxSuccessful()) {
                final String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                logger.info("Successful response: {}", responseBody);
            } else {
                logger.error("Error response, Status code: {}", statusCode);
            }
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            logger.error("Error while sending message: {}", e.getMessage());
            throw new IOException("Error while sending message: " + e.getMessage());
        }
    }

    public void sendMessageWithKeyboard(Update update, String text, ReplyKeyboardMarkup keyboard) throws IOException {
        final String apiUrl = appConfig.getTgBotApiUrl() + botToken + SEND_MESSAGE_ENDPOINT;
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboard);

        final HttpPost request = createHttpPostRequest(apiUrl, sendMessage);
        sendHttpRequest(request);
    }

    public void sendMessage(Long chatId, String message) {
        final String apiUrl = appConfig.getTgBotApiUrl() + botToken +
                SEND_MESSAGE_ENDPOINT + CHAT_ID_ENDPOINT + chatId.toString();
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);
        try {
            sendHttpRequest(createHttpPostRequest(apiUrl, sendMessage));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPhoto(Long chatId, String photoCaption, String photoUrl) {
        final String apiUrl = appConfig.getTgBotApiUrl() + botToken + SEND_PHOTO_ENDPOINT + CHAT_ID_ENDPOINT +
                chatId + "&caption=" + photoCaption;

        final HttpPost request = new HttpPost(apiUrl);
        request.addHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON);

        final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            builder.addPart("photo", new ByteArrayBody(getImageData(photoUrl),
                    ContentType.IMAGE_JPEG, "photo.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        request.setEntity(builder.build());

        try {
            sendHttpRequest(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getImageData(String photoUrl) throws IOException {
        final URL url = new URL(photoUrl);
        try (InputStream input = url.openStream()) {
            return IOUtils.toByteArray(input);
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
