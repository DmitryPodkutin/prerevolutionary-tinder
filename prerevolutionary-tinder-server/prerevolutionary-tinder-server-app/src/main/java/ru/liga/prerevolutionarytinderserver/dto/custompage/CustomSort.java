package ru.liga.prerevolutionarytinderserver.dto.custompage;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class CustomSort {
    private String columnName;
    private Sort.Direction direction;

    public CustomSort() {
    }

    ;

    public CustomSort(String columnName, Sort.Direction direction) {
        this.columnName = columnName;
        this.direction = direction;
    }
}