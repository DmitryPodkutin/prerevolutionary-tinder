package ru.liga.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class UserDto {
    @JsonProperty("telegramId")
    private Long telegramId;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("password")
    private String password;

}
