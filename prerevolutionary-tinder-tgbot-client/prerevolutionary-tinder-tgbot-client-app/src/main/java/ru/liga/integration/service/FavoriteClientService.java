package ru.liga.integration.service;

import ru.liga.dto.FavoriteDto;
import ru.liga.model.User;

import java.util.Optional;

/**
 * Interface representing a service to handle favorites.
 */
public interface FavoriteClientService {

    /**
     * Adds a favorite for the current user.
     *
     * @param favoriteId  ID of the favorite to add
     * @param currentUser The current user
     * @return An optional containing the added FavoriteDto if successful, empty otherwise
     */
    Optional<FavoriteDto> addFavorite(Long favoriteId, User currentUser);
}
