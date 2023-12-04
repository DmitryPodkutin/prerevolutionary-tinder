package ru.liga.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatchingProfileDtoWithImage {
    private Long id;
    private byte[] image;
    private String gender;
    private String name;
    private boolean mutuality;
}
