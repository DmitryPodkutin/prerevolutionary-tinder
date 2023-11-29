package ru.liga.telegrambot.sender.imagesender;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.util.Objects.nonNull;

@Slf4j
@Component
@AllArgsConstructor
public class TelegramImageSender implements ImageSender {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String BOUNDARY = "------Boundary\r\n";
    private static final String CRLF = "\r\n";
    private static final String CONTENT_TYPE_IMAGE_PNG = "Content-Type: image/png\r\n\r\n";
    private static final String STRING = "\"\r\n";
   /* public void sendHttpRequest(byte[] imageBytes, String textMessage, String urlString, Update update) {
        try {
            final HttpURLConnection connection = openConnection(urlString);
            final OutputStream outputStream = connection.getOutputStream();
            final PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

            addFormDataField(writer, "chat_id", getChatId(update).toString());
            addFormDataField(writer, "photo", "fileName" + STRING + CONTENT_TYPE_IMAGE_PNG);
            addImageField(outputStream, imageBytes);
            addTextMessageField(writer, textMessage);

            writer.append("------Boundary--\r\n");
            writer.flush();
            writer.close();

            final InputStream responseStream = connection.getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
            String line;
            final StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpURLConnection openConnection(String urlString) throws IOException {
        final URL url = new URL(urlString);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty(CONTENT_TYPE_HEADER, "multipart/form-data; boundary=----Boundary");
        return connection;
    }

    private void addFormDataField(PrintWriter writer, String fieldName, String fieldValue) {
        writer.append(BOUNDARY)
                .append("Content-Disposition: form-data; name=\"")
                .append(fieldName)
                .append("\"\r\n\r\n")
                .append(fieldValue)
                .append(CRLF);
    }

    private void addImageField(OutputStream outputStream, byte[] imageBytes) throws IOException {
        outputStream.write(imageBytes);
        outputStream.write(CRLF.getBytes());
        outputStream.flush();
    }

    private void addTextMessageField(PrintWriter writer, String textMessage) {
        writer.append(BOUNDARY)
                .append("Content-Disposition: form-data; name=\"text\"\r\n\r\n")
                .append(textMessage)
                .append(CRLF);
    }*/

    private Long getChatId(Update update) {
        if (nonNull(update.getCallbackQuery())) {
            return update.getCallbackQuery().getMessage().getChatId();
        } else {
            return update.getMessage().getChatId();
        }
    }

    public void sendHttpRequest(byte[] imageBytes, String textMessage, String urlString, Update update) {
        try {
            final URL url = new URL(urlString);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty(CONTENT_TYPE_HEADER, "multipart/form-data; boundary=----Boundary");
            final OutputStream outputStream = connection.getOutputStream();
            final PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    outputStream, "UTF-8"), true);
            writer.append(BOUNDARY);
            writer.append("Content-Disposition: form-data; name=\"chat_id\"\r\n\r\n");
            writer.append(getChatId(update).toString()).append(CRLF);
            writer.append(BOUNDARY);
            writer.append("Content-Disposition: form-data; name=\"photo\"; filename=\"" + "fileName" + STRING);
            writer.append(CONTENT_TYPE_IMAGE_PNG);
            writer.flush();
            outputStream.write(imageBytes);
            outputStream.flush();
            writer.append(CRLF);
            writer.append(BOUNDARY);
            writer.append("Content-Disposition: form-data; name=\"text\"\r\n\r\n");
            writer.append(textMessage).append(CRLF);
            writer.append("------Boundary--\r\n");
            writer.flush();
            writer.close();
            final InputStream responseStream = connection.getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
            String line;
            final StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

