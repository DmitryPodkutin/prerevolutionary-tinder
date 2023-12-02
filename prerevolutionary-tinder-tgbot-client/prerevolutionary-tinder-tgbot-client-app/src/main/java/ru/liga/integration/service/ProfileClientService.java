package ru.liga.integration.service;

import org.springframework.http.ResponseEntity;
import ru.liga.dto.MatchingProfileDtoWithImage;
import ru.liga.dto.ProfileDto;
import ru.liga.dto.ProfileDtoWithImage;
import ru.liga.emuns.SwipeDirection;
import ru.liga.model.User;

import java.util.Optional;

public interface ProfileClientService {

    Optional<MatchingProfileDtoWithImage> findNextMatchingProfile(Long telegramId, User user);
    void createProfile(ProfileDto profileDto, User user);
    void updateProfile(ProfileDto profileDto, User user);
    ResponseEntity<ProfileDtoWithImage> getProfile(User user);
    Optional<ProfileDtoWithImage> findNextFavoriteProfile(Long telegramId, User user, SwipeDirection swipeDirection);

}
