package ru.liga.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProfileDto {
    private Long id;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("name")
    private String name;

    @JsonProperty("descriptionHeader")
    private String descriptionHeader;

    @JsonProperty("description")
    private String description;

    @JsonProperty("seekingFor")
    private String seekingFor;

    @JsonProperty("userId")
    private Long userId;
}
