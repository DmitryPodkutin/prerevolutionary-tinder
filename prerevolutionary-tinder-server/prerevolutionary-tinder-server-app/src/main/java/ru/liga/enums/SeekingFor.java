package ru.liga.enums;

import lombok.Getter;

@Getter
public enum SeekingFor {
    SUDAR("Сударъ"),
    SUDARYNYA("Сударыня"),
    ALL("Всех");

    private final String value;

    SeekingFor(String value) {
        this.value = value;
    }
}
