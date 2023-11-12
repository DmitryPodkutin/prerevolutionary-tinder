package ru.liga.enums;

import lombok.Getter;

@Getter
public enum Seeking {
    SUDAR("Сударъ"),
    SUDARYNYA("Сударыня"),
    ALL("Всех");

    private final String value;

    Seeking(String value) {
        this.value = value;
    }
}
