package ru.liga.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class FavoriteDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("user")
    private ProfileDto user;

    @JsonProperty("favorite")
    private ProfileDto favorite;
}
