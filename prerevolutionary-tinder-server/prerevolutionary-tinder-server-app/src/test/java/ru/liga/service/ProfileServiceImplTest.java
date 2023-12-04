package ru.liga.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import ru.liga.dto.ProfileSaveDTO;
import ru.liga.enums.Gender;
import ru.liga.enums.SeekingFor;
import ru.liga.model.Profile;
import ru.liga.repository.ProfileRepository;
import ru.liga.repository.UserRepository;
import ru.liga.service.favourite.FavouriteService;
import ru.liga.service.mutuality.MutualityService;
import ru.liga.service.profile.ProfileServiceImpl;
import ru.liga.service.user.AuthenticationContext;

import java.util.Optional;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ProfileServiceImplTest {

    private ProfileServiceImpl profileService;

    @Mock
    private AuthenticationContext authenticationContext;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConversionService customConversionService;

    @Mock
    private MutualityService mutualityService;

    @Mock
    private FavouriteService favouriteService;


    private ResourceBundle logMessages;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        logMessages = ResourceBundle.getBundle("log_message");
        profileService = new ProfileServiceImpl(profileRepository, authenticationContext,
                userRepository, customConversionService, mutualityService, favouriteService, logMessages);
    }

    @Test
    public void getProfileById_ExistingProfile_ReturnsProfile() {
        Long fakeProfileId = 1L;
        Profile fakeProfile = new Profile();
        when(profileRepository.findById(fakeProfileId)).thenReturn(Optional.of(fakeProfile));

        Optional<Profile> result = profileService.getProfileById(fakeProfileId);

        assertTrue(result.isPresent());
        assertSame(fakeProfile, result.get());
    }

    @Test
    public void getProfileById_NonExistentProfile_ReturnsEmptyOptional() {
        Long fakeProfileId = 1L;
        when(profileRepository.findById(fakeProfileId)).thenReturn(Optional.empty());
        Optional<Profile> result = profileService.getProfileById(fakeProfileId);
        assertTrue(result.isEmpty());
    }

 /*   @Test

    public void createProfile_ValidInput_CreatesProfile() {
        ProfileSaveDTO profileSaveDTO = createSampleProfileSaveDTO();
        profileSaveDTO.setGender("Сударъ");

        Profile createdProfile = profileService.create(profileSaveDTO);

        assertNotNull(createdProfile);
        assertEquals(profileSaveDTO.getName(), createdProfile.getName());
        assertEquals(profileSaveDTO.getDescriptionHeader(), createdProfile.getDescriptionHeader());
        assertEquals(profileSaveDTO.getDescription(), createdProfile.getDescription());
        assertEquals(Gender.MALE, createdProfile.getGender());
        assertEquals(SeekingFor.SUDAR, createdProfile.getSeeking());
    }
*/

    @Test
    public void updateProfile_ExistingProfileAndValidInput_UpdatesProfile() {
        Long fakeProfileId = 1L;
        ProfileSaveDTO profileSaveDTO = createSampleProfileSaveDTO();
        Profile existingProfile = new Profile();
        existingProfile.setName("John");
        existingProfile.setGender(Gender.MALE);
        existingProfile.setDescriptionHeader("About me");
        existingProfile.setDescription("I'm a friendly person.");
        existingProfile.setSeeking(SeekingFor.SUDAR);

        when(profileRepository.findById(fakeProfileId)).thenReturn(Optional.of(existingProfile));

        Profile updatedProfile = profileService.update(profileSaveDTO, fakeProfileId);

        assertNotNull(updatedProfile);
        assertEquals(profileSaveDTO.getName(), updatedProfile.getName());
        assertEquals(profileSaveDTO.getDescriptionHeader(), updatedProfile.getDescriptionHeader());
        assertEquals(profileSaveDTO.getDescription(), updatedProfile.getDescription());
        assertEquals(Gender.MALE, updatedProfile.getGender());
        assertEquals(SeekingFor.SUDAR, updatedProfile.getSeeking());
    }

    @Test
    public void updateProfile_NonExistentProfile_ThrowsEntityNotFoundException() {
        Long fakeProfileId = 1L;
        ProfileSaveDTO profileSaveDTO = createSampleProfileSaveDTO();
        when(profileRepository.findById(fakeProfileId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> profileService.update(profileSaveDTO, fakeProfileId));
    }

    private ProfileSaveDTO createSampleProfileSaveDTO() {
        ProfileSaveDTO profileSaveDTO = new ProfileSaveDTO();
        profileSaveDTO.setName("John");
        profileSaveDTO.setGender("Сударъ");
        profileSaveDTO.setDescriptionHeader("About me");
        profileSaveDTO.setDescription("I'm a friendly person.");
        profileSaveDTO.setSeekingFor("Сударъ");
        return profileSaveDTO;
    }
}
