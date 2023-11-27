package ru.liga.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.liga.enums.SeekingFor;

@Data
@NoArgsConstructor
public class ProfileDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("name")
    private String name;
    @JsonProperty("descriptionHeader")
    private String descriptionHeader;
    @JsonProperty("description")
    private String description;
    @JsonProperty("seeking")
    private SeekingFor seeking;
}
