package ru.liga.dto.filter;

import lombok.Data;
import ru.liga.dto.custompage.CustomSort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class BaseFilter implements Serializable {
    private int limit;
    private int page;
    private List<CustomSort> sortBy = new ArrayList<>();

    public BaseFilter() {
    }

    public BaseFilter(int page, int limit, List<CustomSort> sort) {
        this.page = page;
        this.limit = limit;
        this.sortBy = sort;
    }
}
