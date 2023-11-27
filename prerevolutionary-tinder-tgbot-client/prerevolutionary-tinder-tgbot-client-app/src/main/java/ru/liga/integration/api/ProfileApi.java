package ru.liga.integration.api;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import ru.liga.dto.ProfileDto;
import ru.liga.model.User;

public interface ProfileApi {

    Page<ProfileDto> findMatchingProfiles(Long telegramId, int page, int size);
    ResponseEntity<ProfileDto> createProfile(ProfileDto profileDto, User user);
    ResponseEntity<ProfileDto> updateProfile(ProfileDto profileDto, User user);
    ResponseEntity<ProfileDto> getProfile(User user);
}
