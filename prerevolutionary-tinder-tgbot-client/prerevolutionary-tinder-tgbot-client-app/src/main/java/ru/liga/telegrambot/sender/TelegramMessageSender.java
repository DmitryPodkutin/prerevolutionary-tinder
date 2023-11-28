package ru.liga.telegrambot.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
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
        final String apiUrl = appConfig.getTgBotApiUrl() + botToken + SEND_MESSAGE_ENDPOINT +
                CHAT_ID_ENDPOINT + getChatId(update).toString();
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
        final String messageApiUrl = appConfig.getTgBotApiUrl() + botToken + SEND_MESSAGE_ENDPOINT +
                CHAT_ID_ENDPOINT + getChatId(update).toString();
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(getChatId(update).toString());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboard);

        final HttpPost requestForMessage = createHttpPostRequest(messageApiUrl, sendMessage);

        try {
            sendImageViaHttp(update, image);
            sendHttpRequest(requestForMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendImageViaHttp(Update update, byte[] imageBytes) {
        final RestTemplate restTemplate = new RestTemplate();
        final String telegramApiUrl = appConfig.getTgBotApiUrl() +
                botToken + SEND_PHOTO_ENDPOINT; // Замените на соответствующий URL
        final MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("chat_id", getChatId(update));
        parts.add("photo", new ByteArrayResource(imageBytes));

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parts, headers);

        // Отправка запроса POST для отправки изображения
        final ResponseEntity<String> response = restTemplate.exchange(
                telegramApiUrl, HttpMethod.POST, requestEntity, String.class);
    }


    @Override
    public void openProfileViewKeyboard(Update update, ProfileDtoWithImage profileDtoWithImage) {
        final String profileMessage = formatOutputProfileMessage(profileDtoWithImage);
        sendMessageWithKeyboard(update,
                profileDtoWithImage.getImage(), profileMessage,
                telegramBotKeyboardFactory.createProfileViewKeyboard());
    }

    @Override
    public void openMenuKeyboard(Update update) {
        sendMessageWithKeyboard(update, resourceBundle.getString("menu.message"),
                telegramBotKeyboardFactory.createMenuKeyboard());
    }

    @Override
    public void openSearchSwipeKeyboard(Update update, String message) {
        sendMessageWithKeyboard(update, message,
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
