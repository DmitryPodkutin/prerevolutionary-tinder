package ru.liga.prerevolutionarytinderserver.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public enum Seeking {
    SUDAR("Сударъ"),
    SUDARYNYA("Сударыня"),
    ALL("Всех");

    private final String value;

    Seeking(String value) {
        this.value = value;
    }
}
