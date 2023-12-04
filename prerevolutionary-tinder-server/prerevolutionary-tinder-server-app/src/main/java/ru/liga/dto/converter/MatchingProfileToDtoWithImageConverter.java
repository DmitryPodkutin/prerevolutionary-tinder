package ru.liga.dto.converter;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.liga.dto.MatchingProfileDtoWithImage;
import ru.liga.integration.imagegenerator.ImageGeneratingService;
import ru.liga.integration.texttranslator.TranslationService;
import ru.liga.model.Profile;

@Component
@AllArgsConstructor
public class MatchingProfileToDtoWithImageConverter implements Converter<Profile, MatchingProfileDtoWithImage> {
    private final TranslationService translationService;
    private final ImageGeneratingService imageGeneratingService;

    @Override
    public MatchingProfileDtoWithImage convert(Profile profile) {
        final MatchingProfileDtoWithImage matchingProfileDtoWithImage = new MatchingProfileDtoWithImage();
        matchingProfileDtoWithImage.setId(profile.getId());
        final String translatedDescription = translationService.translateToOldStyle(profile.descriptionToString());
        matchingProfileDtoWithImage.setImage(imageGeneratingService.getProfileImage(
                translatedDescription));
        matchingProfileDtoWithImage.setGender(profile.getGender().getValue());
        matchingProfileDtoWithImage.setName(profile.getName());
        return matchingProfileDtoWithImage;
    }
}
