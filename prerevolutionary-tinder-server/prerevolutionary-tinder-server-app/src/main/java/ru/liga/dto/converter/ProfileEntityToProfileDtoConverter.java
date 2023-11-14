package ru.liga.dto.converter;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.liga.dto.ProfileDto;
import ru.liga.model.Profile;

import static java.util.Objects.nonNull;

@Component
@AllArgsConstructor
public class ProfileEntityToProfileDtoConverter implements Converter<Profile, ProfileDto> {
    @Override
    public ProfileDto convert(Profile profile) {
        final ProfileDto profileDto = new ProfileDto();
        profileDto.setId(profile.getId());
        profileDto.setName(profile.getName());
        profileDto.setDescriptionHeader(profile.getDescriptionHeader());
        if (nonNull(profile.getDescription())) {
            profileDto.setDescription(profile.getDescription());
        }
        profileDto.setGender(profile.getGender().getValue());
        profileDto.setSeeking(profile.getSeeking());
        return profileDto;
    }
}
