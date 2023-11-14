package ru.liga.dto;

import lombok.Data;
import ru.liga.enums.SeekingFor;

@Data
public class ProfileDto {
    private Long id;
    private String gender;
    private String name;
    private String descriptionHeader;
    private String description;
    private SeekingFor seeking;
}
