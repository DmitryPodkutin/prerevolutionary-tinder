package ru.liga.integration.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.liga.dto.MatchingProfileDtoWithImage;
import ru.liga.dto.ProfileDto;
import ru.liga.dto.ProfileDtoWithImage;
import ru.liga.emuns.SwipeDirection;
import ru.liga.integration.api.ProfileApi;
import ru.liga.model.PageInfo;
import ru.liga.model.User;
import ru.liga.service.MatchingProfilesPageInfoService;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ProfileClientServiceImpl implements ProfileClientService {

    public static final int ONE_ELEMENT_PER_PAGE = 1;
    private final ProfileApi profileApi;
    private final MatchingProfilesPageInfoService matchingProfilesPageInfoService;


    @Override
    public Optional<MatchingProfileDtoWithImage> findNextMatchingProfile(Long telegramId, User user) {
        final int currentPage = getCurrentPage(user);
        final Page<MatchingProfileDtoWithImage> matchingProfiles = profileApi.findMatchingProfiles(
                telegramId, currentPage, ONE_ELEMENT_PER_PAGE);
        updateSearchingCurrentPageIfNeeded(user, matchingProfiles.isLast(), currentPage);
        return matchingProfiles.getContent().stream().findFirst();
    }

    @Override
    public Optional<ProfileDtoWithImage> findNextFavoriteProfile(Long telegramId,
                                                                 User user,
                                                                 SwipeDirection swipeDirection) {
        final int currentPage = getFavoriteCurrentPage(user);
        final Page<ProfileDtoWithImage> favoriteProfiles = profileApi.findFavoriteProfiles(
                telegramId, currentPage, ONE_ELEMENT_PER_PAGE);
        updateFavoritesCurrentPageIfNeeded(user, favoriteProfiles.isLast(), favoriteProfiles.isFirst(),
                favoriteProfiles.getTotalPages(),
                currentPage, swipeDirection);
        return favoriteProfiles.getContent().stream().findFirst();
    }

    private int getCurrentPage(User user) {
        return user.getPageInfo() == null ? 0 : user.getPageInfo().getSearchProfileCurrentPage();
    }

    private int getFavoriteCurrentPage(User user) {
        return user.getPageInfo() == null ? 0 : user.getPageInfo().getSearchProfileCurrentPage();
    }

    private void updateSearchingCurrentPageIfNeeded(User user, boolean isLast, int currentPage) {
        PageInfo pageInfo = user.getPageInfo();
        if (pageInfo == null) {
            pageInfo = new PageInfo();
            pageInfo.setSearchProfileCurrentPage(currentPage);
        } else {
            pageInfo.setSearchProfileCurrentPage(isLast ? 0 : currentPage + 1);
        }
        matchingProfilesPageInfoService.save(pageInfo);
    }

    private void updateFavoritesCurrentPageIfNeeded(User user, boolean isLast, boolean isFirst, int totalPages,
                                                    int currentPage, SwipeDirection swipeDirection) {
        PageInfo pageInfo = user.getPageInfo();
        if (pageInfo == null) {
            pageInfo = new PageInfo();
            pageInfo.setFavoriteCurrentPage(currentPage);
        } else {
            if (swipeDirection.equals(SwipeDirection.FORWARD)) {
                pageInfo.setSearchProfileCurrentPage(isLast ? 0 : currentPage + 1);
            } else {
                pageInfo.setSearchProfileCurrentPage(isFirst ?  totalPages - 1 : currentPage - 1);
            }
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
    public ResponseEntity<ProfileDtoWithImage> getProfile(User user) {
        return profileApi.getProfile(user);
    }
}

