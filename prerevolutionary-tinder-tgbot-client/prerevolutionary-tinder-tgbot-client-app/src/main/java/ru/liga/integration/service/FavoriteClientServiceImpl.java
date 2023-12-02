package ru.liga.integration.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.liga.dto.FavoriteDto;
import ru.liga.integration.api.FavoriteApi;
import ru.liga.model.User;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class FavoriteClientServiceImpl implements FavoriteClientService {

    private final FavoriteApi favoriteApi;

    @Override
    public Optional<FavoriteDto> addFavorite(Long favoriteId, User currentUser) {
        final ResponseEntity<FavoriteDto> favoriteDtoResponseEntity = favoriteApi.addFavorite(favoriteId, currentUser);
        return Optional.ofNullable(favoriteDtoResponseEntity.getBody());
    }
}
