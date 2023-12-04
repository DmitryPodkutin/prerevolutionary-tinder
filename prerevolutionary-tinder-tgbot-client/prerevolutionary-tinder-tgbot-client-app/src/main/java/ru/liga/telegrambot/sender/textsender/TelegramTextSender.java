package ru.liga.telegrambot.sender.textsender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class TelegramTextSender implements TextSender {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private final HttpClient httpClient;

    public TelegramTextSender(HttpClient httpClient) {
        this.httpClient = HttpClients.createDefault();
    }
    public TelegramTextSender() {
        this.httpClient = HttpClients.createDefault();
    }

    /**
     * Sends an HTTP request with the provided body to the given API URL.
     *
     * @param apiUrl The URL to which the HTTP request will be sent.
     * @param body   The body of the HTTP request.
     */
    @Override
    public void sendHttpRequest(String apiUrl, Object body) {
        final HttpPost request = createHttpPostRequest(apiUrl, body);
        try {
            final HttpResponse response = httpClient.execute(request);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.valueOf(statusCode).is2xxSuccessful()) {
                final String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                log.info("Successful response: {}", responseBody);
            } else {
                log.error("Error response, Status code: {}", statusCode);
            }
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            log.error("Error while sending message: {}", e.getMessage());
            throw new RuntimeException("Error while sending message: " + e.getMessage());
        }
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
                log.error("Error serializing object to JSON: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return request;
    }
}
