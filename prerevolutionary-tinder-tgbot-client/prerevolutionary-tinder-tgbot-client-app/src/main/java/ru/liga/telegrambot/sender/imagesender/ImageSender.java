package ru.liga.telegrambot.sender.imagesender;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * An interface responsible for sending HTTP requests with image content.
 */
public interface ImageSender {

    /**
     * Sends an HTTP request with the provided image bytes to the specified URL.
     *
     * @param imageBytes The byte array representing the image content to be sent.
     * @param urlString  The URL where the HTTP request will be sent.
     * @param update     The Telegram update object.
     */
    void sendHttpRequest(byte[] imageBytes, String urlString, Update update);
}
