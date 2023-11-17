package ru.liga.prerevolutionarytindertgbotclient.exception;

public class KeyboardCreationError extends RuntimeException {
    public KeyboardCreationError(String message) {
        super("Error while creating keyboard: " + message);
    }
}

