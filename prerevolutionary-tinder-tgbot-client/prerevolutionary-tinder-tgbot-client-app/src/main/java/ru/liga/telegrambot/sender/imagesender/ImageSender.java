package ru.liga.telegrambot.sender.imagesender;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ImageSender {

    void sendHttpRequest(byte[] imageBytes, String textMessage, String urlString, Update update);
}
