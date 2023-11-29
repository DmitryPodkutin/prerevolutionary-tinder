package ru.liga.dto.converter;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import ru.liga.dto.MatchingProfileDTO;
import ru.liga.dto.ProfileDtoWithImage;
import ru.liga.model.Profile;
import ru.liga.service.favourite.FavouriteService;

import java.util.Objects;

@Setter
@Component
@AllArgsConstructor
public class ProfileEntityToMatchingProfileDTOConverter {

    private final FavouriteService favouriteService;
    private final ConversionService customConversionService;

    public MatchingProfileDTO convert(Long currentUserId, Profile profile) {
        final ProfileDtoWithImage profileDtoWithImage = customConversionService.convert(
                profile, ProfileDtoWithImage.class);
        final MatchingProfileDTO matchingProfileDTO = new MatchingProfileDTO();
        BeanUtils.copyProperties(profileDtoWithImage, matchingProfileDTO);
        addMutualFlagIfIntersectsWithFavorites(currentUserId, profile, matchingProfileDTO);
        return matchingProfileDTO;
    }

    private void addMutualFlagIfIntersectsWithFavorites(Long currentUserId, Profile profile,
                                                        MatchingProfileDTO matchingProfileDTO) {
        final boolean isMutual = favouriteService.getAllFavouritesByUserId(currentUserId)
                .stream()
                .anyMatch(favorite -> Objects.equals(favorite.getFavoriteUser().getId(), profile.getUser().getId()));
        matchingProfileDTO.setMutual(isMutual);
    }
}
