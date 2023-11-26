package ru.liga.integration.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.dto.ProfileDto;

@Service
@AllArgsConstructor
public class ProfileClientServiceImpl implements ProfileClientService {

    @Override
    public ProfileDto findMatchingProfiles(Long telegramId) {
        return null;
    }
}
