package ru.liga.service.profile;

import ru.liga.dto.ProfileSaveDTO;
import ru.liga.dto.filter.ProfileFilter;
import ru.liga.model.Profile;

import java.util.Optional;

public interface ProfileService {

    Optional<Profile> getByUserId(Long userId);

    Optional<Profile> getCurrent(ProfileFilter filter);

    Optional<Profile> getProfileById(Long id);

    Profile create(ProfileSaveDTO verifiable);

    Profile update(ProfileSaveDTO profileSaveDTO, Long id);
}
