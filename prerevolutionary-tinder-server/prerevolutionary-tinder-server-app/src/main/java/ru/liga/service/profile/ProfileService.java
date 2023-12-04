package ru.liga.service.profile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.liga.dto.MatchingProfileDtoWithImage;
import ru.liga.dto.ProfileSaveDTO;
import ru.liga.model.Profile;

import java.util.Optional;

public interface ProfileService {

    Page<MatchingProfileDtoWithImage> getAllMatchingProfiles(Pageable pageable);

    Optional<Profile> getByUserId(Long userId);

    Optional<Profile> getCurrent();

    Optional<Profile> getProfileById(Long id);

    Profile create(ProfileSaveDTO verifiable);

    Profile update(ProfileSaveDTO profileSaveDTO, Long id);
}
