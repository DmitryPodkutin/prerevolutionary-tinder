package ru.liga.service.profile;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.dto.ProfileSaveDTO;
import ru.liga.dto.filter.ProfileFilter;
import ru.liga.enums.Gender;
import ru.liga.enums.SeekingFor;
import ru.liga.exception.EntityNotFoundException;
import ru.liga.exception.GenderNotFoundException;
import ru.liga.exception.SeekingForNotFoundException;
import ru.liga.model.Profile;
import ru.liga.repository.ProfileRepository;

import java.util.Arrays;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    public Optional<Profile> getCurrent(ProfileFilter filter) {
        return profileRepository.findByUserId(filter.getUserId());
    }

    @Override
    public Optional<Profile> getByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }

    @Override
    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    @Override
    public Profile create(ProfileSaveDTO profileSaveDTO) {
        final Profile profile = new Profile();
        profile.setName(profileSaveDTO.getName());
        profile.setGender(convertToGender(profileSaveDTO.getGender()));
        profile.setDescriptionHeader(profileSaveDTO.getDescriptionHeader());
        profile.setDescription(profileSaveDTO.getDescription());
        profile.setSeeking(convertToSeekingFor(profileSaveDTO.getSeekingFor()));
        profileRepository.save(profile);
        return profile;
    }

    @Override
    public Profile update(ProfileSaveDTO profileSaveDTO, Long id) {
        final Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        profile.setName(profileSaveDTO.getName());
        profile.setGender(convertToGender(profileSaveDTO.getGender()));
        profile.setDescriptionHeader(profileSaveDTO.getDescriptionHeader());
        profile.setDescription(profileSaveDTO.getDescription());
        profile.setSeeking(convertToSeekingFor(profileSaveDTO.getSeekingFor()));
        profileRepository.save(profile);
        return profile;
    }

    public Gender convertToGender(String genderString) {
        if (genderString == null) {
            throw new GenderNotFoundException("Gender is null or empty");
        }

        return Arrays.stream(Gender.values())
                .filter(g -> g.getValue().equalsIgnoreCase(genderString))
                .findFirst()
                .orElseThrow(() -> new GenderNotFoundException(genderString));
    }

    public SeekingFor convertToSeekingFor(String seekingFor) {
        if (seekingFor == null) {
            throw new SeekingForNotFoundException("SeekingFor is null or empty");
        }

        return Arrays.stream(SeekingFor.values())
                .filter(g -> g.getValue().equalsIgnoreCase(seekingFor))
                .findFirst()
                .orElseThrow(() -> new SeekingForNotFoundException(seekingFor));
    }
}
