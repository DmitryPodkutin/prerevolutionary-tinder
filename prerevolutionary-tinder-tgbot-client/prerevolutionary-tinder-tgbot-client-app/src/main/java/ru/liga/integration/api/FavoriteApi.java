package ru.liga.integration.api;

import org.springframework.http.ResponseEntity;
import ru.liga.dto.FavoriteDto;
import ru.liga.model.User;

public interface FavoriteApi {

    ResponseEntity<FavoriteDto> addFavorite(Long favoriteId, User currentUser);

}
