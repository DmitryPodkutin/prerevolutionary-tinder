package ru.liga.integration.api;

import org.springframework.data.domain.Page;
import ru.liga.dto.ProfileDto;

public interface ProfileApi {

    Page<ProfileDto> findMatchingProfiles(Long telegramId, int page, int size);

}
