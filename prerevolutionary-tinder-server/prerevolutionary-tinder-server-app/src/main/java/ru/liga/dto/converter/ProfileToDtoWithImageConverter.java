package ru.liga.dto.converter;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.liga.dto.ProfileDtoWithImage;
import ru.liga.integration.imagegenerator.ImageGeneratingService;
import ru.liga.integration.texttranslator.TranslationService;
import ru.liga.model.Profile;

@Component
@AllArgsConstructor
public class ProfileToDtoWithImageConverter implements Converter<Profile, ProfileDtoWithImage> {
    private final TranslationService translationService;
    private final ImageGeneratingService imageGeneratingService;

    @Override
    public ProfileDtoWithImage convert(Profile profile) {
        final ProfileDtoWithImage profileDtoWithImage = new ProfileDtoWithImage();
        final String translatedDescription = translationService.translateToOldStyle(profile.getDescription());
        profileDtoWithImage.setImage(imageGeneratingService.fetchImageFromRemoteService(
                translatedDescription).getBody());
        profileDtoWithImage.setGender(profile.getGender().getValue());
        profileDtoWithImage.setName(profile.getName());
        return profileDtoWithImage;
    }
}
