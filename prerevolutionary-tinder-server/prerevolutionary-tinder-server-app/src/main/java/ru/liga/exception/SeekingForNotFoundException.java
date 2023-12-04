package ru.liga.exception;

public class SeekingForNotFoundException extends RuntimeException {

    public SeekingForNotFoundException (String seekingFor) {
        super("Gender not found for value: " + seekingFor);
    }
}
