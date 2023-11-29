package ru.liga.dto;

import lombok.Data;

@Data
public class MatchingProfileDTO extends ProfileDtoWithImage {
    private boolean isMutual;
}
