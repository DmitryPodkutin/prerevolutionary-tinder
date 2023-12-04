package ru.liga.integration.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.liga.dto.FavoriteDto;
import ru.liga.integration.api.FavoriteApi;
import ru.liga.model.User;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Service implementation for handling favorite operations.
 */
@Slf4j
@Service
@AllArgsConstructor
public class FavoriteClientServiceImpl implements FavoriteClientService {

    private final FavoriteApi favoriteApi;
    private final ResourceBundle logMessages;

    /**
     * Adds a favorite for the current user.
     *
     * @param favoriteId  ID of the favorite to add
     * @param currentUser The current user
     * @return An optional containing the added FavoriteDto if successful, empty otherwise
     */
    @Override
    public Optional<FavoriteDto> addFavorite(Long favoriteId, User currentUser) {
        try {
            final ResponseEntity<FavoriteDto> favoriteDtoResponseEntity =
                    favoriteApi.addFavorite(favoriteId, currentUser);
            final FavoriteDto favoriteDto = favoriteDtoResponseEntity.getBody();
            if (favoriteDto != null) {
                log.info(logMessages.getString("info.favorite.added"), favoriteDto.getId());
            } else {
                log.warn(logMessages.getString("warn.favorite.added.failed"), favoriteId);
            }
            return Optional.ofNullable(favoriteDto);
        } catch (Exception e) {
            log.error(logMessages.getString("error.favorite.adding"), favoriteId, e);
            return Optional.empty();
        }
    }
}
