package ru.liga.service.profile;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.dto.ProfileSaveDTO;
import ru.liga.dto.converter.ProfileEntityToMatchingProfileDTOConverter;
import ru.liga.dto.MatchingProfileDTO;
import ru.liga.dto.filter.ProfileFilter;
import ru.liga.enums.Gender;
import ru.liga.enums.SeekingFor;
import ru.liga.exception.EntityNotFoundException;
import ru.liga.exception.GenderNotFoundException;
import ru.liga.exception.SeekingForNotFoundException;
import ru.liga.model.AuthorizedUser;
import ru.liga.model.Profile;
import ru.liga.repository.ProfileRepository;
import ru.liga.repository.UserRepository;
import ru.liga.service.user.AuthenticationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final AuthenticationContext authenticationContext;
    private final ProfileEntityToMatchingProfileDTOConverter entityToMatchingProfileDTOConverter;
    private final UserRepository userRepository;

    @Override
    public List<MatchingProfileDTO> getAllMatchingProfiles() {
        final AuthorizedUser currentUser = authenticationContext.getCurrentUser();
        final List<Profile> matchingProfiles = profileRepository.findMatchingProfiles(
                currentUser.getProfile().getGender(), currentUser.getProfile().getSeeking());
        return matchingProfiles.stream().map((Profile profile) ->
                        entityToMatchingProfileDTOConverter.convert(currentUser.getUserId(), profile))
                .collect(Collectors.toList());
    }

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
        profile.setUser(userRepository.findById(authenticationContext.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(authenticationContext.getCurrentUserId())));
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
