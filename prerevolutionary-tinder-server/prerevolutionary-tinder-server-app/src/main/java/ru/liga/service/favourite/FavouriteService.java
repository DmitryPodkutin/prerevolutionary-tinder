package ru.liga.service.favourite;

import ru.liga.dto.filter.FavouriteFilter;
import ru.liga.model.Favorite;

import java.util.List;
import java.util.Optional;

public interface FavouriteService {
    List<Favorite> findFavouritesByFilter(FavouriteFilter filter);

    Optional<Favorite> getOne(Long id);
}
