package ru.liga.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.liga.dto.ProfileSaveDTO;
import ru.liga.enums.Gender;
import ru.liga.enums.SeekingFor;
import ru.liga.exception.EntityNotFoundException;
import ru.liga.exception.GenderNotFoundException;
import ru.liga.exception.SeekingForNotFoundException;
import ru.liga.model.Profile;
import ru.liga.repository.ProfileRepository;
import ru.liga.service.profile.ProfileServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ProfileServiceImplTest {

    private ProfileServiceImpl profileService;

    @Mock
    private ProfileRepository profileRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        profileService = new ProfileServiceImpl(profileRepository);
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

    @Test
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

    @Test
    public void updateProfile_ExistingProfileAndValidInput_UpdatesProfile() {
        Long fakeProfileId = 1L;
        ProfileSaveDTO profileSaveDTO = createSampleProfileSaveDTO();
        Profile existingProfile = new Profile();
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

        assertThrows(EntityNotFoundException.class, () -> profileService.update(profileSaveDTO, fakeProfileId));
    }

    @Test
    public void convertToGender_ValidInput_ReturnsGenderEnum() {
        String genderString = "Сударъ";
        Gender expectedGender = Gender.MALE;

        Gender convertedGender = profileService.convertToGender(genderString);

        assertEquals(expectedGender, convertedGender);
    }

    @Test
    public void convertToGender_InvalidInput_ThrowsGenderNotFoundException() {
        String genderString = "UnknownGender";

        assertThrows(GenderNotFoundException.class, () -> profileService.convertToGender(genderString));
    }

    @Test
    public void convertToSeekingFor_ValidInput_ReturnsSeekingForEnum() {
        String seekingForString = "Сударъ";
        SeekingFor expectedSeekingFor = SeekingFor.SUDAR;

        SeekingFor convertedSeekingFor = profileService.convertToSeekingFor(seekingForString);

        assertEquals(expectedSeekingFor, convertedSeekingFor);
    }

    @Test
    public void convertToSeekingFor_InvalidInput_ThrowsSeekingForNotFoundException() {
        String seekingForString = "UnknownSeekingFor";

        assertThrows(SeekingForNotFoundException.class, () -> profileService.convertToSeekingFor(seekingForString));
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
