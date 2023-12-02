package ru.liga.service.mutuality;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.enums.Gender;
import ru.liga.enums.Mutuality;
import ru.liga.model.Favorite;
import ru.liga.model.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MutualityServiceImpl implements MutualityService {

    @Override
    public Mutuality addMutualFlagIfIntersectsWithFavorites(Long currentUserId, Profile profile,
                                                            List<Favorite> allFavouritesByUserId,
                                                            List<Favorite> allFavoritesByFavoriteUserUd) {
        final List<Long> myFavorites = allFavouritesByUserId.stream()
                .map(favorite -> favorite.getFavoriteUser().getId())
                .collect(Collectors.toList());
        final List<Long> iAmFavoriteFor = allFavoritesByFavoriteUserUd.stream()
                .map(favorite -> favorite.getUser().getId())
                .collect(Collectors.toList());

        final List<Long> mutualFavorites = new ArrayList<>(myFavorites);
        mutualFavorites.retainAll(iAmFavoriteFor);
        if (mutualFavorites.contains(profile.getUser().getId())) {
            return Mutuality.MUTUAL;
        }
        final List<Long> uniqueFromMyFavorites = new ArrayList<>(myFavorites);
        uniqueFromMyFavorites.removeAll(mutualFavorites);
        if (uniqueFromMyFavorites.contains(profile.getUser().getId())) {
            if (profile.getGender().equals(Gender.MALE)) {
                return Mutuality.YOU_LIKE_HIM;
            } else {
                return Mutuality.YOU_LIKE_HER;
            }
        } else {
            return Mutuality.LIKE_YOU;
        }
    }

    @Override
    public boolean isMutual(Long firstUserId, List<Favorite> firstUserFavorites,
                            Long secondUserId, List<Favorite> secondUserFavorites) {
        return isUserInFavorites(firstUserId, firstUserFavorites) &&
                isUserInFavorites(secondUserId, secondUserFavorites);
    }

    private boolean isUserInFavorites(Long userId, List<Favorite> allFavouritesByUserId) {
        return allFavouritesByUserId
                .stream()
                .anyMatch(favorite -> Objects.equals(favorite.getFavoriteUser().getId(), userId));
    }

}

