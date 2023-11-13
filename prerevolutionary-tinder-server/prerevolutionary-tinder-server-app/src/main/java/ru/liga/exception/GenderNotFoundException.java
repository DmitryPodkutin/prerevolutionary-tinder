package ru.liga.exception;

public class GenderNotFoundException extends RuntimeException {
    public GenderNotFoundException(String genderString) {
        super("Gender not found for value: " + genderString);
    }
}
