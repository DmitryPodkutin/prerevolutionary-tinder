package ru.liga.dto.converter;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.liga.dto.FavoriteDto;
import ru.liga.exception.EntityNotFoundException;
import ru.liga.model.Favorite;
import ru.liga.repository.ProfileRepository;

@Component
@AllArgsConstructor
public class FavouriteEntityToDtoConverter implements Converter<Favorite, FavoriteDto> {
    private final ProfileRepository profileRepository;
    @Override
    public FavoriteDto convert(Favorite favorite) {
        final FavoriteDto favoriteDto = new FavoriteDto();
        favoriteDto.setId(favorite.getId());
        favoriteDto.setUser(profileRepository.findByUserId(favorite.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException(favorite.getUser().getId())));
        favoriteDto.setFavorite(profileRepository.findByUserId(favorite.getFavoriteUser().getId())
                .orElseThrow(() -> new EntityNotFoundException(favorite.getFavoriteUser().getId())));
        return favoriteDto;
    }
}
