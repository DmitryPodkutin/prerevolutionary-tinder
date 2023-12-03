package ru.liga.integration.api;

import org.springframework.http.ResponseEntity;
import ru.liga.dto.FavoriteDto;
import ru.liga.model.User;

/**
 * Interface that defines the methods for interacting with Favorite-related APIs.
 */
public interface FavoriteApi {

    /**
     * Adds a user with the specified favoriteId to the current user's favorites.
     *
     * @param favoriteId  The ID of the user to be added to favorites.
     * @param currentUser The current user making the request.
     * @return A ResponseEntity containing the result of adding the user to favorites.
     */
    ResponseEntity<FavoriteDto> addFavorite(Long favoriteId, User currentUser);

}
