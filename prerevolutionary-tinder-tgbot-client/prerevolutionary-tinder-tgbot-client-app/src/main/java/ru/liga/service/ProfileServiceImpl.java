package ru.liga.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.model.Profile;
import ru.liga.repository.ProfileRepository;

import java.util.Optional;

/**
 * Implementation of the ProfileService responsible for managing profile-related operations.
 */
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;


    /**
     * Saves a profile.
     *
     * @param profile The profile to be saved.
     * @return The saved profile.
     */
    @Override
    public Profile saveProfile(Profile profile) {
        return profileRepository.saveAndFlush(profile);
    }

    /**
     * Retrieves a profile by its ID.
     *
     * @param id The ID of the profile.
     * @return An Optional containing the profile if found, otherwise empty.
     */
    @Override
    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    /**
     * Retrieves a profile by its chat ID.
     *
     * @param chatId The chat ID associated with the profile.
     * @return An Optional containing the profile if found, otherwise empty.
     */
    @Override
    public Optional<Profile> getByChatId(Long chatId) {
        return profileRepository.findByChatId(chatId);
    }

    /**
     * Deletes a temporary profile.
     *
     * @param profile The temporary profile to be deleted.
     */
    @Override
    public void deleteTempProfile(Profile profile) {
        profileRepository.delete(profile);
    }
}
