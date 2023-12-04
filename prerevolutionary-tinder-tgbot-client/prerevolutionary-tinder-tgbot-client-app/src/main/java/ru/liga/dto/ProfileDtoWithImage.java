package ru.liga.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ProfileDtoWithImage {
    private Long id;
    private byte[] image;
    private String gender;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mutuality;
}
