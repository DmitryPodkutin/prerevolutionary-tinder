package ru.liga.service.favourite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.liga.dto.FavoriteProfileDTO;
import ru.liga.dto.filter.FavouriteFilter;
import ru.liga.model.Favorite;

import java.util.List;
import java.util.Optional;

public interface FavouriteService {

    List<Favorite> getAllFavouritesByUserId(Long useId);

    List<Favorite> findFavouritesByFilter(FavouriteFilter filter);

    Optional<Favorite> getOne(Long id);

    List<Favorite> getAllFavoritesByFavoriteUserUd(Long currentUserId);

    Page<FavoriteProfileDTO> findFavourites(Pageable pageable);
}
