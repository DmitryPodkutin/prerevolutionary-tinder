package ru.liga.integration.service;

import ru.liga.dto.ProfileDto;

public interface ProfileClientService {

    ProfileDto findMatchingProfiles(Long telegramId);
}
