package ru.liga.dto.converter;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.liga.dto.MatchingProfileDTO;
import ru.liga.model.Profile;
import ru.liga.service.favourite.FavouriteService;

import java.util.Objects;

import static java.util.Objects.nonNull;

@Setter
@Component
@AllArgsConstructor
public class ProfileEntityToMatchingProfileDTOConverter {

    private final FavouriteService favouriteService;

    public MatchingProfileDTO convert(Long currentUserId, Profile profile) {
        final MatchingProfileDTO matchingProfileDTO = new MatchingProfileDTO();
        matchingProfileDTO.setId(profile.getId());
        matchingProfileDTO.setName(profile.getName());
        matchingProfileDTO.setDescriptionHeader(profile.getDescriptionHeader());
        if (nonNull(profile.getDescription())) {
            matchingProfileDTO.setDescription(profile.getDescription());
        }
        matchingProfileDTO.setGender(profile.getGender().getValue());
        matchingProfileDTO.setSeeking(profile.getSeeking());
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
