package ru.liga.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProfileFilter extends BaseFilter {

    private final String gender;
    private String name;
    private String descriptionHeader;
    private String description;
    private String seeking;
    private Long userId;
}
