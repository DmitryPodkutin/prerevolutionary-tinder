package ru.liga.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDto {
    @JsonProperty("telegramId")
    private final Long telegramId;
    @JsonProperty("username")
    private final String userName;
    @JsonProperty("password")
    private final String password;
}
