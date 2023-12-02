package ru.liga.integration.service;

import ru.liga.dto.FavoriteDto;
import ru.liga.model.User;

import java.util.Optional;

public interface FavoriteClientService {

    Optional<FavoriteDto> addFavorite(Long favoriteId, User currentUser);
}
