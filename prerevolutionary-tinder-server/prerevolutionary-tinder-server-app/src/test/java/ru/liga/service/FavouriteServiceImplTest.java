package ru.liga.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import ru.liga.dto.converter.ProfileEntityToFavoriteProfileDTOConverter;
import ru.liga.dto.filter.FavouriteFilter;
import ru.liga.model.Favorite;
import ru.liga.model.User;
import ru.liga.repository.FavouriteRepository;
import ru.liga.repository.ProfileRepository;
import ru.liga.repository.UserRepository;
import ru.liga.service.favourite.FavouriteServiceImpl;
import ru.liga.service.mutuality.MutualityService;
import ru.liga.service.user.AuthenticationContext;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class FavouriteServiceImplTest {
    private FavouriteServiceImpl favouriteService;

    @Mock
    private FavouriteRepository favouriteRepository;

    @Mock
    private AuthenticationContext authenticationContext;

    @Mock
    private ProfileEntityToFavoriteProfileDTOConverter profileEntityToFavoriteProfileDTOConverter;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MutualityService mutualityService;
    @Mock
    private ResourceBundle logMessage;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        favouriteService = new FavouriteServiceImpl(favouriteRepository, authenticationContext,
                profileEntityToFavoriteProfileDTOConverter, profileRepository, userRepository,
                mutualityService, logMessage);
    }

    @Test
    public void testFindFavouritesByFilter() {
        User user = new User();
        user.setId(1L);

        User userFavorite = new User();
        userFavorite.setId(3L);

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setFavoriteUser(userFavorite);

        FavouriteFilter filter = new FavouriteFilter(user, favorite);

        List<Favorite> fakeFavourites = List.of(favorite);
        when(favouriteRepository.findAllByUserAndFavoriteUser(user.getId(),
                Sort.unsorted())).thenReturn(fakeFavourites);

        List<Favorite> result = favouriteService.findFavouritesByFilter(filter);

        assertEquals(fakeFavourites, result);
    }

    @Test
    public void testGetOne() {
        Long fakeFavoriteId = 1L;
        Favorite fakeFavorite = new Favorite();
        when(favouriteRepository.findById(fakeFavoriteId)).thenReturn(Optional.of(fakeFavorite));

        Optional<Favorite> result = favouriteService.getOne(fakeFavoriteId);

        assertEquals(Optional.of(fakeFavorite), result);
    }
}
