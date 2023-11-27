package ru.liga.integration.service;

import org.springframework.http.ResponseEntity;
import ru.liga.dto.ProfileDto;
import ru.liga.model.User;

public interface ProfileClientService {

    ProfileDto findMatchingProfiles(Long telegramId);
    void createProfile(ProfileDto profileDto, User user);
    ResponseEntity<ProfileDto> getProfile(User user);

}
