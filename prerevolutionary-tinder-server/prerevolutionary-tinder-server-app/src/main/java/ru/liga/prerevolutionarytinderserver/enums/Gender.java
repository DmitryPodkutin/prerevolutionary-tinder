package ru.liga.prerevolutionarytinderserver.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    MALE("Сударъ"),
    FEMALE("Сударыня");

    private final String value;

    Gender(String value) {
        this.value = value;
    }
}