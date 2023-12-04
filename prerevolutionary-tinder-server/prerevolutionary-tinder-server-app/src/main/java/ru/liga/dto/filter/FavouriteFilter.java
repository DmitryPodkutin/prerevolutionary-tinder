package ru.liga.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.liga.model.Favorite;
import ru.liga.model.User;

@EqualsAndHashCode(callSuper = true)
@Data
public class FavouriteFilter extends BaseFilter {
    private final User user;
    private final Favorite favorite;
}

