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
import java.util.ResourceBundle;

@Slf4j
@Service
@AllArgsConstructor
public class ProfileClientServiceImpl implements ProfileClientService {

    public static final int ONE_ELEMENT_PER_PAGE = 1;
    private final ProfileApi profileApi;
    private final MatchingProfilesPageInfoService matchingProfilesPageInfoService;
    private final ResourceBundle logMessages;

    @Override
    public Optional<MatchingProfileDtoWithImage> findNextMatchingProfile(Long telegramId, User user) {
        try {
            final int currentPage = getCurrentPage(user);
            final Page<MatchingProfileDtoWithImage> matchingProfiles = profileApi.findMatchingProfiles(
                    telegramId, currentPage, ONE_ELEMENT_PER_PAGE);
            if (matchingProfiles.hasContent()) {
                updateSearchingCurrentPageIfNeeded(user, matchingProfiles.isLast(), currentPage);
                final MatchingProfileDtoWithImage firstMatchingProfile = matchingProfiles.getContent().get(0);
                log.info(logMessages.getString("info.matching.profile.found"), firstMatchingProfile.getId());
                return Optional.of(firstMatchingProfile);
            } else {
                log.warn(logMessages.getString("warn.matching.profile.not.found"), currentPage);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error(logMessages.getString("error.matching.profile.finding"), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<ProfileDtoWithImage> findNextFavoriteProfile(Long telegramId,
                                                                 User user,
                                                                 SwipeDirection swipeDirection) {
        try {
            final int currentPage = getFavoriteCurrentPage(user);
            final Page<ProfileDtoWithImage> favoriteProfiles = profileApi.findFavoriteProfiles(
                    telegramId, currentPage, ONE_ELEMENT_PER_PAGE);
            if (favoriteProfiles.hasContent()) {
                updateFavoritesCurrentPageIfNeeded(user, favoriteProfiles.isLast(), favoriteProfiles.isFirst(),
                        favoriteProfiles.getTotalPages(),
                        currentPage, swipeDirection);
                final ProfileDtoWithImage firstFavoriteProfile = favoriteProfiles.getContent().get(0);
                log.info(logMessages.getString("info.favorite.profile.found"), firstFavoriteProfile.getId());
                return Optional.of(firstFavoriteProfile);
            } else {
                log.warn(logMessages.getString("warn.favorite.profile.not.found"), currentPage);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error(logMessages.getString("error.favorite.profile.finding"), e);
            return Optional.empty();
        }
    }

    @Override
    public void createProfile(ProfileDto profileDto, User user) {
        try {
            profileApi.createProfile(profileDto, user);
            log.info(logMessages.getString("info.profile.created.successfully"),
                    user.getId(), profileDto.getName());
        } catch (Exception e) {
            log.error(logMessages.getString("error.creating.profile"), profileDto.getName(), e);
            throw e;
        }
    }

    @Override
    public void updateProfile(ProfileDto profileDto, User user) {
        try {
            final Long serverProfileId = profileApi.getProfile(user).getBody().getId();
            profileApi.updateProfile(profileDto, user, serverProfileId);
            log.info(logMessages.getString("info.profile.updated.successfully"), user.getId());
        } catch (Exception e) {
            log.error(logMessages.getString("error.updating.profile"), user.getId(), e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<ProfileDtoWithImage> getProfile(User user) {
        try {
            final ResponseEntity<ProfileDtoWithImage> response = profileApi.getProfile(user);
            log.info(logMessages.getString("info.profile.retrieved.successfully"), user.getId());
            return response;
        } catch (Exception e) {
            log.error(logMessages.getString("error.retrieving.profile"), user.getId(), e);
            throw e;
        }
    }

    private int getCurrentPage(User user) {
        return user.getPageInfo() == null ? 0 : user.getPageInfo().getSearchProfileCurrentPage();
    }

    private int getFavoriteCurrentPage(User user) {
        return user.getPageInfo() == null ? 0 : user.getPageInfo().getFavoriteCurrentPage();
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
                pageInfo.setFavoriteCurrentPage(isLast ? 0 : currentPage + 1);
            } else {
                pageInfo.setFavoriteCurrentPage(isFirst ? totalPages - 1 : currentPage - 1);
            }
        }
        matchingProfilesPageInfoService.save(pageInfo);
    }
}

