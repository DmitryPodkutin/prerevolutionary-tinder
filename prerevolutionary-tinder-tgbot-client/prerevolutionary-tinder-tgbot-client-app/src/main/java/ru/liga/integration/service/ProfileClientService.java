package ru.liga.integration.service;

import org.springframework.http.ResponseEntity;
import ru.liga.dto.MatchingProfileDtoWithImage;
import ru.liga.dto.ProfileDto;
import ru.liga.dto.ProfileDtoWithImage;
import ru.liga.emuns.SwipeDirection;
import ru.liga.model.User;

import java.util.Optional;

/**
 * Service interface for handling profile operations.
 */
public interface ProfileClientService {

    /**
     * Finds the next matching profile based on a given user.
     *
     * @param telegramId ID related to the user
     * @param user       The user object
     * @return An optional containing the matching profile with an image if found, empty otherwise
     */
    Optional<MatchingProfileDtoWithImage> findNextMatchingProfile(Long telegramId, User user);

    /**
     * Creates a profile for a user.
     *
     * @param profileDto The profile DTO to create
     * @param user       The user object
     */
    void createProfile(ProfileDto profileDto, User user);

    /**
     * Updates a profile for a user.
     *
     * @param profileDto The profile DTO to update
     * @param user       The user object
     */
    void updateProfile(ProfileDto profileDto, User user);

    /**
     * Retrieves a profile for a user.
     *
     * @param user The user object
     * @return ResponseEntity with a profile DTO along with an image
     */
    ResponseEntity<ProfileDtoWithImage> getProfile(User user);

    /**
     * Finds the next favorite profile based on a given user and swipe direction.
     *
     * @param telegramId     ID related to the user
     * @param user           The user object
     * @param swipeDirection The direction of the swipe (forward or backward)
     * @return An optional containing the favorite profile with an image if found, empty otherwise
     */Optional<ProfileDtoWithImage> findNextFavoriteProfile(Long telegramId, User user, SwipeDirection swipeDirection);

}
