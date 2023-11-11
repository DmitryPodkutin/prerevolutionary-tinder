package ru.liga.prerevolutionarytinderserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Seeking {
    SUDAR("Сударъ"),
    SUDARYNYA("Сударыня"),
    ALL("Всех");

    private final String value;
}
