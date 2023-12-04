package ru.liga.dto;

import lombok.Data;

@Data
public class UserSaveDTO {
    private Long id;
    private String userName;
    private String password;
}
