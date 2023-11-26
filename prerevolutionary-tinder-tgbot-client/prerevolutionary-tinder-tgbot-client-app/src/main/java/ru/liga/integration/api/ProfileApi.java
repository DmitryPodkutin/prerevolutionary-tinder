package ru.liga.integration.api;

import ru.liga.dto.ProfileDto;

public interface ProfileApi {

    ProfileDto findMatchingProfiles(Long telegramId);

}
