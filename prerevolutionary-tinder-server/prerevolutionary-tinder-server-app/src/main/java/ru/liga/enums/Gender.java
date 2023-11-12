package ru.liga.enums;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("Сударъ"),
    FEMALE("Сударыня");

    private final String value;

    Gender(String value) {
        this.value = value;
    }
}