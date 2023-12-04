package ru.liga.dto.converter;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import ru.liga.dto.FavoriteProfileDTO;
import ru.liga.dto.ProfileDtoWithImage;
import ru.liga.enums.Mutuality;
import ru.liga.model.Profile;

@Setter
@Component
@AllArgsConstructor
public class ProfileEntityToFavoriteProfileDTOConverter {

    private final ConversionService customConversionService;

    public FavoriteProfileDTO convert(Profile profile, Mutuality mutuality) {
        final ProfileDtoWithImage profileDtoWithImage = customConversionService.convert(
                profile, ProfileDtoWithImage.class);
        final FavoriteProfileDTO favoriteProfileDTO = new FavoriteProfileDTO();
        BeanUtils.copyProperties(profileDtoWithImage, favoriteProfileDTO);
        favoriteProfileDTO.setMutuality(mutuality.getValue());
        return favoriteProfileDTO;
    }
}
