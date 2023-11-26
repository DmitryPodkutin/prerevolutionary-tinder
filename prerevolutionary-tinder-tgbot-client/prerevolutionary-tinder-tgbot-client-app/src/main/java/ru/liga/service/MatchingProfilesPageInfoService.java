package ru.liga.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.model.MatchingProfilesPageInfo;
import ru.liga.repository.MatchingProfilesPageInfoRepository;

@Service
@AllArgsConstructor
public class MatchingProfilesPageInfoService {

    private final MatchingProfilesPageInfoRepository pageInfoRepository;

    public MatchingProfilesPageInfo save(MatchingProfilesPageInfo pageInfo) {
        return pageInfoRepository.save(pageInfo);
    }

    public MatchingProfilesPageInfo getByUserId(Long userId) {
        return pageInfoRepository.findByUserId(userId).orElseThrow(
                () -> new RuntimeException("MatchingProfilesPageInfo not found for user with ID: " + userId));
    }
}
