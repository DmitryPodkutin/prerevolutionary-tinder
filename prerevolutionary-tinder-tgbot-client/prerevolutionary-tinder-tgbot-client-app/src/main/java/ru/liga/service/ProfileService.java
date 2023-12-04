package ru.liga.service;

import ru.liga.model.Profile;

import java.util.Optional;

/**
 * The service interface responsible for managing profile-related operations.
 */
public interface ProfileService {

    /**
     * Saves a profile.
     *
     * @param profile The profile to be saved.
     * @return The saved profile.
     */
    Profile saveProfile(Profile profile);


    /**
     * Retrieves a profile by its ID.
     *
     * @param id The ID of the profile.
     * @return An Optional containing the profile if found, otherwise empty.
     */
    Optional<Profile> getProfileById(Long id);

    /**
     * Retrieves a profile by its chat ID.
     *
     * @param chatId The chat ID associated with the profile.
     * @return An Optional containing the profile if found, otherwise empty.
     */
    Optional<Profile> getByChatId(Long chatId);

    /**
     * Deletes a temporary profile.
     *
     * @param profile The temporary profile to be deleted.
     */
    void deleteTempProfile(Profile profile);
}
