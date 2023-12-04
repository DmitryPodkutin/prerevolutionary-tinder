package ru.liga.dto;

import lombok.Data;
import ru.liga.model.Profile;

@Data
public class FavoriteDto {
    private Long id;
    private Profile user;
    private Profile favorite;
}
