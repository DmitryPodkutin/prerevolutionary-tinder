package ru.liga.integration.service;

import org.springframework.http.ResponseEntity;
import ru.liga.dto.ProfileDto;
import ru.liga.dto.ProfileDtoWithImage;
import ru.liga.model.User;

import java.util.Optional;

public interface ProfileClientService {

    Optional<ProfileDtoWithImage> findNextMatchingProfiles(Long telegramId, User user);
    void createProfile(ProfileDto profileDto, User user);
    void updateProfile(ProfileDto profileDto, User user);
    ResponseEntity<ProfileDtoWithImage> getProfile(User user);

}
