package ru.liga.integration.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.liga.dto.ProfileDto;
import ru.liga.integration.api.ProfileApi;
import ru.liga.model.PageInfo;
import ru.liga.model.User;
import ru.liga.service.MatchingProfilesPageInfoService;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ProfileClientServiceImpl implements ProfileClientService {

    private final ProfileApi profileApi;
    private final MatchingProfilesPageInfoService matchingProfilesPageInfoService;


    @Override
    public Optional<ProfileDto> findNextMatchingProfiles(Long telegramId, User user) {
        final int currentPage = getCurrentPage(user);
        final int oneElementPerPage = 1;
        final Page<ProfileDto> matchingProfiles = profileApi.findMatchingProfiles(
                telegramId, currentPage, oneElementPerPage);
        updateCurrentPageIfNeeded(user, matchingProfiles.isLast(), currentPage);
        return matchingProfiles.getContent().stream().findFirst();
    }

    private int getCurrentPage(User user) {
        return user.getPageInfo() == null ? 0 : user.getPageInfo().getSearchProfileCurrentPage();
    }

    private void updateCurrentPageIfNeeded(User user, boolean isLast, int currentPage) {
        PageInfo pageInfo = user.getPageInfo();
        if (pageInfo == null) {
            pageInfo = new PageInfo();
            pageInfo.setSearchProfileCurrentPage(currentPage);
        } else {
            pageInfo.setSearchProfileCurrentPage(isLast ? 0 : currentPage + 1);
        }
        matchingProfilesPageInfoService.save(pageInfo);
    }

    @Override
    public void createProfile(ProfileDto profileDto, User user) {
        profileApi.createProfile(profileDto, user);
    }

    @Override
    public void updateProfile(ProfileDto profileDto, User user) {
        final Long serverProfileId = profileApi.getProfile(user).getBody().getId();
        profileApi.updateProfile(profileDto, user, serverProfileId);
    }

    @Override
    public ResponseEntity<ProfileDto> getProfile(User user) {
        return profileApi.getProfile(user);
    }
}

