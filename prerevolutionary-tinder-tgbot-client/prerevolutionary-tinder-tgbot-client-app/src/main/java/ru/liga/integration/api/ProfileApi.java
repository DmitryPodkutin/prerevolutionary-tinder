package ru.liga.integration.api;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import ru.liga.dto.MatchingProfileDtoWithImage;
import ru.liga.dto.ProfileDto;
import ru.liga.dto.ProfileDtoWithImage;
import ru.liga.model.User;


/**
 * Interface to communicate with the Profile API.
 */
public interface ProfileApi {

    /**
     * Finds matching profiles for a user based on the given parameters.
     *
     * @param telegramId        The Telegram ID of the user.
     * @param currentPage       The current page number.
     * @param oneElementPerPage The number of elements per page.
     * @return A Page containing matching profiles.
     */
    Page<MatchingProfileDtoWithImage> findMatchingProfiles(Long telegramId, int currentPage, int oneElementPerPage);

    /**
     * Creates a new profile for the user.
     *
     * @param profileDto The profile DTO containing profile information.
     * @param user       The user creating the profile.
     * @return A ResponseEntity containing the created profile with image.
     */
    ResponseEntity<ProfileDtoWithImage> createProfile(ProfileDto profileDto, User user);

    /**
     * Updates an existing profile for the user.
     *
     * @param profileDto The profile DTO containing updated profile information.
     * @param user       The user updating the profile.
     * @param id         The ID of the profile to be updated.
     * @return A ResponseEntity containing the updated profile with image.
     */
    ResponseEntity<ProfileDtoWithImage> updateProfile(ProfileDto profileDto, User user, Long id);

    /**
     * Retrieves the profile of the user.
     *
     * @param user The user for whom the profile needs to be retrieved.
     * @return A ResponseEntity containing the user's profile with image.
     */
    ResponseEntity<ProfileDtoWithImage> getProfile(User user);


    /**
     * Finds favorite profiles for a user based on the given parameters.
     *
     * @param telegramId        The Telegram ID of the user.
     * @param currentPage       The current page number.
     * @param oneElementPerPage The number of elements per page.
     * @return A Page containing favorite profiles.
     */
    Page<ProfileDtoWithImage> findFavoriteProfiles(Long telegramId, int currentPage, int oneElementPerPage);
}
