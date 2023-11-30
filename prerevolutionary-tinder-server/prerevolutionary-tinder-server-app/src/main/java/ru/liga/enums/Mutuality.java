package ru.liga.enums;

import lombok.Getter;

@Getter
public enum Mutuality {
    MUTUAL("Взаимность"),
    LIKE_YOU("Вы любимы"),
    YOU_LIKE_HIM("Любим вами"),
    YOU_LIKE_HER("Любима вами");

    private final String value;

    Mutuality(String value) {
        this.value = value;
    }

}
