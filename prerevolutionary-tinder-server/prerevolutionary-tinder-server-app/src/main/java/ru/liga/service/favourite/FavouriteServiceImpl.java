package ru.liga.service.favourite;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.liga.dto.FavoriteProfileDTO;
import ru.liga.dto.converter.ProfileEntityToFavoriteProfileDTOConverter;
import ru.liga.dto.filter.FavouriteFilter;
import ru.liga.enums.Gender;
import ru.liga.enums.Mutuality;
import ru.liga.model.AuthorizedUser;
import ru.liga.model.Favorite;
import ru.liga.model.Profile;
import ru.liga.repository.FavouriteRepository;
import ru.liga.repository.ProfileRepository;
import ru.liga.repository.UserRepository;
import ru.liga.service.user.AuthenticationContext;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FavouriteServiceImpl implements FavouriteService {

    private final FavouriteRepository favouriteRepository;
    private final AuthenticationContext authenticationContext;
    private final ProfileEntityToFavoriteProfileDTOConverter profileEntityToFavoriteProfileDTOConverter;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    public List<Favorite> getAllFavouritesByUserId(Long userId) {
        return favouriteRepository.findAllByUserId(userId);
    }

    @Override
    public List<Favorite> getAllFavoritesByFavoriteUserUd(Long currentUserId) {
        return favouriteRepository.findAllFavoritesByFavoritesUserId(currentUserId);
    }

    @Override
    public Page<FavoriteProfileDTO> findFavourites(Pageable pageable) {
        final AuthorizedUser currentUser = authenticationContext.getCurrentUser();

        final Page<Favorite> favoritePage = favouriteRepository.findMatchingProfiles(
                userRepository.findById(currentUser.getUserId())
                        .orElseThrow(EntityNotFoundException::new), pageable);
        return convertFavoriteToProfile(favoritePage, currentUser.getUserId()).map(profile ->
                profileEntityToFavoriteProfileDTOConverter.convert(profile,
                        addMutualFlagIfIntersectsWithFavorites(currentUser.getUserId(), profile)));
    }

    public List<Favorite> findFavouritesByFilter(FavouriteFilter filter) {
        return favouriteRepository.findAllByUserAndFavoriteUser(filter.getUser().getId(), getSort(filter));
    }

    @Override
    public Optional<Favorite> getOne(Long id) {
        return favouriteRepository.findById(id);
    }

    private Sort getSort(FavouriteFilter filter) {
        final Sort sort = Sort.by(filter.getSortBy().stream()
                .map(customSort -> new Sort.Order(customSort.getDirection(), customSort.getColumnName()))
                .collect(Collectors.toList()));
        return sort;
    }

    private Page<Profile> convertFavoriteToProfile(Page<Favorite> favorites, Long currentUserId) {
        final List<Profile> profiles = new ArrayList<>();
        favorites.forEach(favorite -> {
            if (favorite.getFavoriteUser().getId().equals(currentUserId)) {
                profiles.add(profileRepository.findByUserId(favorite.getUser().getId())
                        .orElseThrow(EntityNotFoundException::new));
            } else {
                profiles.add(profileRepository.findByUserId(favorite.getFavoriteUser().getId())
                        .orElseThrow(EntityNotFoundException::new));
            }
        });
        return new PageImpl<>(profiles, favorites.getPageable(), favorites.getTotalElements());
    }

    private Mutuality addMutualFlagIfIntersectsWithFavorites(Long currentUserId, Profile profile) {
        final List<Long> myFavorites = getAllFavouritesByUserId(currentUserId).stream()
                .map(favorite -> favorite.getFavoriteUser().getId())
                .collect(Collectors.toList());
        final List<Long> iAmFavoriteFor = getAllFavoritesByFavoriteUserUd(currentUserId).stream()
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
}
