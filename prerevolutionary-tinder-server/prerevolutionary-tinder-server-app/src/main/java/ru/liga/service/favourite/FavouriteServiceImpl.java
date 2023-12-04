package ru.liga.service.favourite;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.liga.dto.FavoriteProfileDTO;
import ru.liga.dto.converter.ProfileEntityToFavoriteProfileDTOConverter;
import ru.liga.dto.filter.FavouriteFilter;
import ru.liga.model.AuthorizedUser;
import ru.liga.model.Favorite;
import ru.liga.model.Profile;
import ru.liga.repository.FavouriteRepository;
import ru.liga.repository.ProfileRepository;
import ru.liga.repository.UserRepository;
import ru.liga.service.mutuality.MutualityService;
import ru.liga.service.user.AuthenticationContext;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class FavouriteServiceImpl implements FavouriteService {

    private final FavouriteRepository favouriteRepository;
    private final AuthenticationContext authenticationContext;
    private final ProfileEntityToFavoriteProfileDTOConverter profileEntityToFavoriteProfileDTOConverter;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final MutualityService mutualityService;
    private final ResourceBundle logMessages;

    @Override
    public List<Favorite> getAllFavouritesByUserId(Long userId) {
        try {
            final List<Favorite> favorites = favouriteRepository.findAllByUserId(userId);
            log.info(logMessages.getString("retrieved.favorites.with.id"), favorites.size(), userId);
            return favorites;
        } catch (Exception e) {
            log.error(logMessages.getString("error.retrieved.favorites.with.id"), userId, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Favorite> getAllFavoritesByFavoriteUserUd(Long currentUserId) {
        try {
            final List<Favorite> favorites = favouriteRepository.findAllFavoritesByFavoritesUserId(currentUserId);
            log.info(logMessages.getString("find.favorites.with.ud"), favorites.size(), currentUserId);
            return favorites;
        } catch (Exception e) {
            log.error(logMessages.getString("error.find.favorites.with.id"), currentUserId, e);
            return Collections.emptyList();
        }
    }

    @Override
    public Page<FavoriteProfileDTO> findFavourites(Pageable pageable) {
        try {
            final AuthorizedUser currentUser = authenticationContext.getCurrentUser();
            final Page<Favorite> favoritePage = favouriteRepository.findMatchingProfiles(
                    userRepository.findById(currentUser.getUserId())
                            .orElseThrow(EntityNotFoundException::new), pageable);
            log.info(logMessages.getString("find.favorites.with.id"), favoritePage.getTotalElements(),
                    currentUser.getUserId());
            return convertFavoriteToProfile(favoritePage, currentUser.getUserId()).map(profile ->
                    profileEntityToFavoriteProfileDTOConverter.convert(profile,
                            mutualityService.addMutualFlagIfIntersectsWithFavorites(currentUser.getUserId(), profile,
                                    getAllFavouritesByUserId(currentUser.getUserId()),
                                    getAllFavoritesByFavoriteUserUd(currentUser.getUserId()))));
        } catch (Exception e) {
            log.error(logMessages.getString("error.find.favorites"), e);
            return Page.empty();
        }
    }

    @Override
    public Favorite createFavorite(Long favoriteUserId) {
        try {
            final AuthorizedUser currentUser = authenticationContext.getCurrentUser();
            if (checkIfAlreadyFavorite(currentUser.getUserId(), favoriteUserId)) {
                log.info(logMessages.getString("favorite.already.exists.with.id"),
                        currentUser.getUserId(), favoriteUserId);
                return favouriteRepository.findAllByUserId(currentUser.getUserId()).stream()
                        .filter(favorite -> favorite.getFavoriteUser().getId().equals(favoriteUserId)).findFirst()
                        .orElseThrow(EntityNotFoundException::new);
            }
            log.info(logMessages.getString("favorite.created.successfully"),
                    currentUser.getUserId(), favoriteUserId);
            return createFavorite(currentUser.getUserId(), favoriteUserId);
        } catch (Exception e) {
            log.error(logMessages.getString("error.creating.favorite"), e);
            throw e;
        }
    }

    @Override
    public List<Favorite> findFavouritesByFilter(FavouriteFilter filter) {
        try {
            return favouriteRepository.findAllByUserAndFavoriteUser(filter.getUser().getId(), getSort(filter));
        } catch (Exception e) {
            log.error(logMessages.getString("error.finding.favorites.by.filter"), e);
            throw e;
        }
    }

    @Override
    public Optional<Favorite> getOne(Long id) {
        try {
            return favouriteRepository.findById(id);
        } catch (Exception e) {
            log.error(logMessages.getString("error.finding.favorites.by.id"), id, e);
            throw e;
        }
    }

    private Sort getSort(FavouriteFilter filter) {
        return Sort.by(filter.getSortBy().stream()
                .map(customSort -> new Sort.Order(customSort.getDirection(), customSort.getColumnName()))
                .collect(Collectors.toList()));
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

    private Favorite createFavorite(Long currentUser, Long favoriteUserId) {
        final Favorite favorite = new Favorite();
        favorite.setUser(userRepository.findById(currentUser)
                .orElseThrow(EntityNotFoundException::new));
        favorite.setFavoriteUser(userRepository.findById(favoriteUserId)
                .orElseThrow(EntityNotFoundException::new));
        favouriteRepository.save(favorite);
        return favorite;
    }

    private Boolean checkIfAlreadyFavorite(Long currentUserId, Long favoriteId) {
        if (favouriteRepository.findAllByUserId(currentUserId).stream()
                .map(favorite -> favorite.getFavoriteUser().getId()).collect(Collectors.toList())
                .contains(favoriteId)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
