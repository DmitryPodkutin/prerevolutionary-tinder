package ru.liga.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.model.PageInfo;
import ru.liga.repository.MatchingProfilesPageInfoRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ResourceBundle;

/**
 * Service responsible for managing PageInfo related to matching profiles.
 * This service interacts with the repository to perform CRUD operations on PageInfo entities.
 */

@Slf4j
@Service
@AllArgsConstructor
public class MatchingProfilesPageInfoService {

    private final MatchingProfilesPageInfoRepository pageInfoRepository;
    private final ResourceBundle logMessages;

    /**
     * Saves the provided PageInfo entity.
     *
     * @param pageInfo The PageInfo entity to be saved.
     * @return The saved PageInfo entity.
     */
    public PageInfo save(PageInfo pageInfo) {
        return pageInfoRepository.save(pageInfo);
    }

    /**
     * Retrieves PageInfo based on the user ID.
     *
     * @param userId The ID of the user to retrieve PageInfo for.
     * @return The PageInfo associated with the provided user ID.
     * @throws RuntimeException if the matching PageInfo is not found for the given user ID.
     */
    public PageInfo getByUserId(Long userId) {
        try {
            return pageInfoRepository.findByUserId(userId).orElseThrow(() ->
                    new EntityNotFoundException("MatchingProfilesPageInfo not found for user with ID: " + userId));
        } catch (EntityNotFoundException e) {
            final String errorMessage = logMessages.getString("error.matching.profile.info.retrieve") + userId;
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }
}
