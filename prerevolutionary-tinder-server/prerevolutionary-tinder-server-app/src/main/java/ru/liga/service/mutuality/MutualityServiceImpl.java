package ru.liga.service.mutuality;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.enums.Gender;
import ru.liga.enums.Mutuality;
import ru.liga.model.Favorite;
import ru.liga.model.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MutualityServiceImpl implements MutualityService {

    private final ResourceBundle logMessages;

    @Override
    public Mutuality addMutualFlagIfIntersectsWithFavorites(Long currentUserId, Profile profile,
                                                            List<Favorite> allFavouritesByUserId,
                                                            List<Favorite> allFavoritesByFavoriteUserUd) {
        try {
            final List<Long> myFavorites = allFavouritesByUserId.stream()
                    .map(favorite -> favorite.getFavoriteUser().getId())
                    .collect(Collectors.toList());
            final List<Long> iAmFavoriteFor = allFavoritesByFavoriteUserUd.stream()
                    .map(favorite -> favorite.getUser().getId())
                    .collect(Collectors.toList());
            final List<Long> mutualFavorites = new ArrayList<>(myFavorites);
            mutualFavorites.retainAll(iAmFavoriteFor);
            if (mutualFavorites.contains(profile.getUser().getId())) {
                log.info(logMessages.getString("mutual.found.for.use.id"), currentUserId);
                return Mutuality.MUTUAL;
            }
            final List<Long> uniqueFromMyFavorites = new ArrayList<>(myFavorites);
            uniqueFromMyFavorites.removeAll(mutualFavorites);
            if (uniqueFromMyFavorites.contains(profile.getUser().getId())) {
                log.info(logMessages.getString("one.sided.connection.found"), currentUserId);
                return profile.getGender().equals(Gender.MALE) ? Mutuality.YOU_LIKE_HIM : Mutuality.YOU_LIKE_HER;
            } else {
                log.info(logMessages.getString("no.connection.found"), currentUserId);
                return Mutuality.LIKE_YOU;
            }
        } catch (Exception e) {
            log.error(logMessages.getString("error.mutual.found.for.use.id"), currentUserId, e);
            throw e;
        }
    }

    @Override
    public boolean isMutual(Long firstUserId, List<Favorite> firstUserFavorites,
                            Long secondUserId, List<Favorite> secondUserFavorites) {
        try {
            final boolean mutual = isUserInFavorites(firstUserId, firstUserFavorites) &&
                    isUserInFavorites(secondUserId, secondUserFavorites);
            if (mutual) {
                log.info(logMessages.getString("mutual.found.for.use.ids"), firstUserId, secondUserId);
            } else {
                log.info(logMessages.getString("no.mutual.connection.found"), firstUserId, secondUserId);
            }
            return mutual;
        } catch (Exception e) {
            log.error(logMessages.getString("error.mutual.found.for.use.ids"), firstUserId, secondUserId, e);
            throw e;
        }
    }

    private boolean isUserInFavorites(Long userId, List<Favorite> allFavouritesByUserId) {
        return allFavouritesByUserId
                .stream()
                .anyMatch(favorite -> Objects.equals(favorite.getFavoriteUser().getId(), userId));
    }

}

