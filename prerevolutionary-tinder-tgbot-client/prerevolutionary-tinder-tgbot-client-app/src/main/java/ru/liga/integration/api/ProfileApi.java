package ru.liga.integration.api;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import ru.liga.dto.ProfileDto;
import ru.liga.dto.ProfileDtoWithImage;
import ru.liga.model.User;

public interface ProfileApi {

    Page<ProfileDtoWithImage> findMatchingProfiles(Long telegramId, int currentPage, int oneElementPerPage);
    ResponseEntity<ProfileDtoWithImage> createProfile(ProfileDto profileDto, User user);
    ResponseEntity<ProfileDtoWithImage> updateProfile(ProfileDto profileDto, User user, Long id);
    ResponseEntity<ProfileDtoWithImage> getProfile(User user);

    Page<ProfileDtoWithImage> findFavoriteProfiles(Long telegramId, int currentPage, int oneElementPerPage);
}
