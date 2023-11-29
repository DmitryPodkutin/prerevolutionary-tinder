package ru.liga.telegrambot.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.liga.config.AppConfig;
import ru.liga.dto.ProfileDtoWithImage;
import ru.liga.telegrambot.keyboard.TelegramBotKeyboardFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import static java.util.Objects.nonNull;

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
            final ObjectMapper objectMapper = new ObjectMapper();
            try {
                final String jsonBody = objectMapper.writeValueAsString(body);
                final StringEntity params = new StringEntity(jsonBody, StandardCharsets.UTF_8);
                request.setEntity(params);
            } catch (JsonProcessingException e) {
                // Обработка ошибки сериализации объекта в JSON
                e.printStackTrace();
            }
        }

        return request;
    }

    private void sendHttpPhotoRequest(HttpPost request) throws IOException {
        try {
            final HttpResponse response = httpClient.execute(request);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.valueOf(statusCode).is2xxSuccessful()) {
                logger.info("Success response: {}", response.getEntity());
            } else {
                logger.error("Error response, Status: {}", statusCode);
            }
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            logger.error("Error sending message: {}", e.getMessage());
            throw new IOException(e.getMessage());
        }
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

    public void sendMessageWithKeyboard(Update update, String text, InlineKeyboardMarkup keyboard) {
        final String apiUrl = appConfig.getTgBotApiUrl() + botToken +
                SEND_MESSAGE_ENDPOINT + CHAT_ID_ENDPOINT + getChatId(update).toString();
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(getChatId(update).toString());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboard);
        final HttpPost request = createHttpPostRequest(apiUrl, sendMessage);
        try {
            sendHttpRequest(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public void sendMessageWithKeyboard(Update update, byte[] image, String text, InlineKeyboardMarkup keyboard) {
        final String messageApiUrl = appConfig.getTgBotApiUrl() + botToken +
                SEND_MESSAGE_ENDPOINT + CHAT_ID_ENDPOINT + getChatId(update).toString();
        final String photoApiUrl = appConfig.getTgBotApiUrl() + botToken +
                SEND_PHOTO_ENDPOINT + CHAT_ID_ENDPOINT + getChatId(update).toString();
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(getChatId(update).toString());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboard);

        final HttpPost requestForMessage = createHttpPostRequest(messageApiUrl, sendMessage);

        try {
            sendImageHttpRequest(image, photoApiUrl);
            sendHttpRequest(requestForMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

/*    public void sendPhoto(Long chatId, String photoCaption, String photoUrl) {
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
    }*/


/*    public HttpPost createHttpPostRequestForImage(byte[] image, String apiUrl) {

        final HttpPost request = new HttpPost(apiUrl);
        request.setHeader(CONTENT_TYPE_HEADER, "image/png");

        final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        // Устанавливаем параметры Content-Disposition
        builder.addBinaryBody("attachment", (image), ContentType.IMAGE_PNG, "questionnaire.png");

        final HttpEntity multipart = builder.build();
        request.setEntity(multipart);

        return request;
    }*/

    private void sendImageHttpRequest(byte[]  imageData, String url) {
        final CloseableHttpClient httpClient1 = HttpClients.createDefault();

        final HttpPost request = new HttpPost(url);

        request.setHeader(CONTENT_TYPE_HEADER, "image/png");
        // Другие заголовки могут быть установлены через request.setHeader()

        // Установка тела запроса в виде массива байтов
        final HttpEntity entity = new ByteArrayEntity(imageData);
        request.setEntity(entity);

        try (CloseableHttpResponse response = httpClient1.execute(request)) {
            // Обработка ответа, если необходимо
            final int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Response Status Code: " + statusCode);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void openProfileViewKeyboard(Update update, ProfileDtoWithImage profileDtoWithImage) {
        final String profileMessage = formatOutputProfileMessage(profileDtoWithImage);
        sendMessageWithKeyboard(update, profileDtoWithImage.getImage(), profileMessage,
                telegramBotKeyboardFactory.createProfileViewKeyboard());
    }

    @Override
    public void openMenuKeyboard(Update update) {
        sendMessageWithKeyboard(update, resourceBundle.getString("menu.message"),
                telegramBotKeyboardFactory.createMenuKeyboard());
    }

    @Override
    public void openSearchSwipeKeyboard(Update update, String message) {
        sendMessageWithKeyboard(update, message, telegramBotKeyboardFactory.createSwipeKeyboard());
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
