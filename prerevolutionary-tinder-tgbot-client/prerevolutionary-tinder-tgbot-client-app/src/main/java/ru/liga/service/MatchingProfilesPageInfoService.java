package ru.liga.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.model.PageInfo;
import ru.liga.repository.MatchingProfilesPageInfoRepository;

@Service
@AllArgsConstructor
public class MatchingProfilesPageInfoService {

    private final MatchingProfilesPageInfoRepository pageInfoRepository;

    public PageInfo save(PageInfo pageInfo) {
        return pageInfoRepository.save(pageInfo);
    }

    public PageInfo getByUserId(Long userId) {
        return pageInfoRepository.findByUserId(userId).orElseThrow(
                () -> new RuntimeException("MatchingProfilesPageInfo not found for user with ID: " + userId));
    }
}
