package ru.liga.dto;

import lombok.Data;

@Data
public class ProfileDtoWithImage {
    private Long serverProfileId;
    private byte[] image;
    private String gender;
    private String name;
}
