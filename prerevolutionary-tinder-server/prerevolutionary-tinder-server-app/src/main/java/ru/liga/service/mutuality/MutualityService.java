package ru.liga.service.mutuality;

import ru.liga.enums.Mutuality;
import ru.liga.model.Favorite;
import ru.liga.model.Profile;

import java.util.List;

public interface MutualityService {

    Mutuality addMutualFlagIfIntersectsWithFavorites(Long currentUserId, Profile profile,
                                                     List<Favorite> allFavouritesByUserId,
                                                     List<Favorite> allFavoritesByFavoriteUserUd);

    boolean isMutual(Long firstUserId,
                     List<Favorite> firstUserFavorites,
                     Long secondUserId,
                     List<Favorite> secondUserFavorites);
}
