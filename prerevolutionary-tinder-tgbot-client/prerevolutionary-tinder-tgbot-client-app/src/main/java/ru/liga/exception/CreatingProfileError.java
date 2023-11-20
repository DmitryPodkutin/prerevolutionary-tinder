package ru.liga.exception;

public class CreatingProfileError extends RuntimeException {
    public CreatingProfileError(String message) {
        super("Error while creating profile: " + message);
    }
}

