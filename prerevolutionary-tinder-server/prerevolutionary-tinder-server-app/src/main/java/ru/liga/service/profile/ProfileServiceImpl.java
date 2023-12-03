package ru.liga.service.profile;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.liga.dto.MatchingProfileDtoWithImage;
import ru.liga.dto.ProfileSaveDTO;
import ru.liga.enums.Gender;
import ru.liga.enums.SeekingFor;
import ru.liga.exception.EntityNotFoundException;
import ru.liga.exception.GenderNotFoundException;
import ru.liga.exception.SeekingForNotFoundException;
import ru.liga.model.AuthorizedUser;
import ru.liga.model.Favorite;
import ru.liga.model.Profile;
import ru.liga.repository.ProfileRepository;
import ru.liga.repository.UserRepository;
import ru.liga.service.favourite.FavouriteService;
import ru.liga.service.mutuality.MutualityService;
import ru.liga.service.user.AuthenticationContext;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
@Slf4j
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final AuthenticationContext authenticationContext;
    private final UserRepository userRepository;
    private final ConversionService customConversionService;
    private final MutualityService mutualityService;
    private final FavouriteService favouriteService;
    private final ResourceBundle logMessages;

    @Override
    public Page<MatchingProfileDtoWithImage> getAllMatchingProfiles(Pageable pageable) {
        final AuthorizedUser currentUser = authenticationContext.getCurrentUser();
        try {
            final Page<Profile> matchingProfiles = profileRepository.findMatchingProfiles(
                    fillSeekingFor(currentUser), fillGenderForLookingFor(currentUser),
                    userRepository.findById(currentUser.getUserId())
                            .orElseThrow(EntityExistsException::new),
                    pageable);
            log.info(logMessages.getString("matching.profiles.retrieved"), currentUser.getUserId());
            return matchingProfiles.map(profile -> convertToMatchingProfileDto(profile, currentUser.getUserId()));
        } catch (Exception e) {
            log.error(logMessages.getString("error.retrieving.matching.profiles"), e);
            return Page.empty();
        }
    }

    @Override
    public Optional<Profile> getCurrent() {
        try {
            final Long currentUserId = authenticationContext.getCurrentUserId();
            final Optional<Profile> profile = profileRepository.findByUserId(currentUserId);
            if (profile.isPresent()) {
                log.info(logMessages.getString("current.profile.retrieved"), currentUserId);
            } else {
                log.warn(logMessages.getString("current.profile.not.found"), currentUserId);
            }
            return profile;
        } catch (Exception e) {
            log.error(logMessages.getString("error.retrieving.current.profile"), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Profile> getByUserId(Long userId) {
        try {
            final Optional<Profile> profile = profileRepository.findByUserId(userId);
            if (profile.isPresent()) {
                log.info("Successfully retrieved profile for user with ID: {}", userId);
            } else {
                log.warn(logMessages.getString("profile.not.found"), userId);
            }
            return profile;
        } catch (Exception e) {
            log.error(logMessages.getString("error.retrieving.profile.by.user.id"), userId, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Profile> getProfileById(Long id) {
        try {
            final Optional<Profile> profile = profileRepository.findById(id);
            if (profile.isPresent()) {
                log.info(logMessages.getString("profile.found"), id);
            } else {
                log.warn(logMessages.getString("profile.found.with.id"), id);
            }
            return profile;
        } catch (Exception e) {
            log.error(logMessages.getString("error.retrieving.profile.by.id"), id, e);
            return Optional.empty();
        }
    }


    @Override
    public Profile create(ProfileSaveDTO profileSaveDTO) {
        try {
            final Profile profile = new Profile();
            profile.setName(profileSaveDTO.getName());
            profile.setGender(convertToGender(profileSaveDTO.getGender()));
            profile.setDescriptionHeader(profileSaveDTO.getDescriptionHeader());
            profile.setDescription(profileSaveDTO.getDescription());
            profile.setSeeking(convertToSeekingFor(profileSaveDTO.getSeekingFor()));
            profile.setUser(userRepository.findById(authenticationContext.getCurrentUserId())
                    .orElseThrow(() -> new EntityNotFoundException(authenticationContext.getCurrentUserId())));
            profileRepository.save(profile);

            log.info(logMessages.getString("profile.created"), profile.getId());

            return profile;
        } catch (Exception e) {
            log.error(logMessages.getString("error.creating.profile"), e);
            throw e;
        }
    }

    @Override
    public Profile update(ProfileSaveDTO profileSaveDTO, Long id) {
        try {
            final Profile profile = profileRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(id));
            profile.setName(profileSaveDTO.getName());
            profile.setGender(convertToGender(profileSaveDTO.getGender()));
            profile.setDescriptionHeader(profileSaveDTO.getDescriptionHeader());
            profile.setDescription(profileSaveDTO.getDescription());
            profile.setSeeking(convertToSeekingFor(profileSaveDTO.getSeekingFor()));
            profileRepository.save(profile);
            log.info(logMessages.getString("profile.updated"), id);
            return profile;
        } catch (RuntimeException e) {
            log.error(logMessages.getString("error.updating.profile"), id, e);
            throw e;
        }
    }

    public Gender convertToGender(String genderString) {
        if (genderString == null) {
            throw new GenderNotFoundException(logMessages.getString("gender.is.null.or.empty"));
        }

        return Arrays.stream(Gender.values())
                .filter(g -> g.getValue().equalsIgnoreCase(genderString))
                .findFirst()
                .orElseThrow(() -> new GenderNotFoundException(genderString));
    }

    public SeekingFor convertToSeekingFor(String seekingFor) {
        if (seekingFor == null) {
            throw new SeekingForNotFoundException(logMessages.getString("seeking.for.is.null.or.empty"));
        }

        return Arrays.stream(SeekingFor.values())
                .filter(g -> g.getValue().equalsIgnoreCase(seekingFor))
                .findFirst()
                .orElseThrow(() -> new SeekingForNotFoundException(seekingFor));
    }

    private List<Gender> fillGenderForLookingFor(AuthorizedUser user) {
        final List<Gender> seekingFor = new ArrayList<>();
        switch (user.getProfile().getSeeking()) {
            case SUDAR:
                seekingFor.add(Gender.MALE);
            case SUDARYNYA:
                seekingFor.add(Gender.FEMALE);
            default:
                seekingFor.add(Gender.FEMALE);
                seekingFor.add(Gender.MALE);
        }
        return seekingFor;
    }

    private SeekingFor fillSeekingFor(AuthorizedUser user) {
        switch (user.getProfile().getGender()) {
            case MALE:
                return SeekingFor.SUDAR;
            default:
                return SeekingFor.SUDARYNYA;
        }
    }

    private MatchingProfileDtoWithImage convertToMatchingProfileDto(Profile profile, Long currentUserId) {
        final MatchingProfileDtoWithImage matchingProfileDtoWithImage = customConversionService.convert(profile,
                MatchingProfileDtoWithImage.class);
        if (matchingProfileDtoWithImage != null) {
            final boolean isMutual = mutualityService.isMutual(profile.getId(), getFavorites(currentUserId),
                    currentUserId, getFavorites(profile.getId()));
            matchingProfileDtoWithImage.setMutuality(isMutual);
        }
        return matchingProfileDtoWithImage;
    }

    private List<Favorite> getFavorites(Long currentUserId) {
        return favouriteService.getAllFavouritesByUserId(currentUserId);
    }
}
