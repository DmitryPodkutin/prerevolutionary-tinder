package ru.liga.telegrambot.sender.textsender;


/**
 * A generic interface for sending HTTP requests with text content.
 */
public interface TextSender {

    /**
     * Sends an HTTP request with the provided body to the given API URL.
     *
     * @param apiUrl The URL to which the HTTP request will be sent.
     * @param body   The body of the HTTP request.
     */
    void sendHttpRequest (String apiUrl, Object body);
}
