package ru.liga.controller;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.liga.dto.MatchingProfileDTO;
import ru.liga.dto.ProfileDto;
import ru.liga.dto.ProfileSaveDTO;
import ru.liga.service.profile.ProfileService;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final ConversionService customConversionService;

    @GetMapping
    public ResponseEntity<ProfileDto> getCurrent() {
        return profileService.getCurrent()
                .map(profile -> new ResponseEntity<>(customConversionService.convert(
                        profile, ProfileDto.class),
                        HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDto> getProfileById(@PathVariable Long id) {
        return profileService.getProfileById(id)
                .map(profile -> new ResponseEntity<>(customConversionService.convert(
                        profile, ProfileDto.class),
                        HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "")
    public ResponseEntity<ProfileDto> create(@RequestBody ProfileSaveDTO profileSaveDTO) {
        return new ResponseEntity<>(customConversionService.convert(
                profileService.create(profileSaveDTO), ProfileDto.class),
                HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProfileDto> update(@NotNull @PathVariable("id") Long id,
                                             @RequestBody ProfileSaveDTO profileSaveDTO) {
        return new ResponseEntity<>(customConversionService.convert(
                profileService.update(profileSaveDTO, id), ProfileDto.class),
                HttpStatus.OK);
    }

    @GetMapping("/matching")
    public ResponseEntity<Page<MatchingProfileDTO>> findMatchingProfiles(@RequestParam() int page,
                                                                         @RequestParam() int size) {
        final Page<MatchingProfileDTO> matchingProfiles =
                profileService.getAllMatchingProfiles(PageRequest.of(page, size));
        if (matchingProfiles.hasContent()) {
            return new ResponseEntity<>(matchingProfiles, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
