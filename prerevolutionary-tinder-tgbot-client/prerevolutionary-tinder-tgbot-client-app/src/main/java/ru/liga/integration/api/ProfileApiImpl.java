package ru.liga.integration.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.liga.dto.MatchingProfileDtoWithImage;
import ru.liga.dto.ProfileDto;
import ru.liga.dto.ProfileDtoWithImage;
import ru.liga.integration.component.CustomPageImpl;
import ru.liga.integration.config.RestClientConfig;
import ru.liga.model.User;
import ru.liga.service.UserService;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class ProfileApiImpl implements ProfileApi {

    private static final String PAGE_PARAM = "page";
    private static final String SIZE_PARAM = "size";
    private final RestClientConfig restClientConfig;
    private final RestTemplate restTemplate;
    private final UserService userService;

    @Override
    public ResponseEntity<ProfileDtoWithImage> getProfile(User user) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(user.getUserName(), user.getPassword());
            headers.setContentType(MediaType.APPLICATION_JSON);
            return restTemplate.exchange(
                    restClientConfig.getProfileServiceUrl(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<>() {
                    });
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Profile not found for URL: {}", restClientConfig.getProfileServiceUrl());
        } catch (HttpClientErrorException.BadRequest e) {
            log.error("Bad request to profile service: {}", e.getMessage());
        } catch (HttpClientErrorException.Unauthorized e) {
            log.error("Unauthorized access to profile service: {}", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public Page<MatchingProfileDtoWithImage> findMatchingProfiles(Long telegramId, int page, int size) {
        try {
            final User currentUser = getCurrentuser(telegramId);
            final HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(currentUser.getUserName(), currentUser.getPassword());
            final UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(restClientConfig.getProfileMatchingServiceUrl())
                    .queryParam(PAGE_PARAM, page)
                    .queryParam(SIZE_PARAM, size);
            final ResponseEntity<CustomPageImpl<MatchingProfileDtoWithImage>> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<>() {
                    });
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            log.error("HTTP error during remote service call to get matching profiles: {}", e.getRawStatusCode(), e);
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);
        } catch (RuntimeException e) {
            log.error("Error during remote service call to get matching profile", e);
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);
        }
    }

    @Override
    public Page<ProfileDtoWithImage> findFavoriteProfiles(Long telegramId, int page, int size) {
        try {
            final User currentUser = getCurrentuser(telegramId);
            final HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(currentUser.getUserName(), currentUser.getPassword());
            final UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(restClientConfig.getFavoriteServiceUrl())
                    .queryParam(PAGE_PARAM, page)
                    .queryParam(SIZE_PARAM, size);
            final ResponseEntity<CustomPageImpl<ProfileDtoWithImage>> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<>() {
                    });
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            log.error("HTTP error during remote service call to get favorites: {}", e.getRawStatusCode(), e);
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);
        } catch (RuntimeException e) {
            log.error("Error during remote service call to get favorites", e);
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);
        }
    }

    @Override
    public ResponseEntity<ProfileDtoWithImage> createProfile(ProfileDto profileDto, User user) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(user.getUserName(), user.getPassword());
            headers.setContentType(MediaType.APPLICATION_JSON);
            final HttpEntity<ProfileDto> requestEntity = new HttpEntity<>(profileDto, headers);
            return restTemplate.postForEntity(
                    restClientConfig.getProfileServiceUrl(), requestEntity, ProfileDtoWithImage.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Profile service URL to create not found: {}", e.getMessage());
        } catch (HttpClientErrorException.BadRequest e) {
            log.error("Bad request to profile create service: {}", e.getMessage());
        } catch (HttpClientErrorException.Unauthorized e) {
            log.error("Unauthorized access to profile create service: {}", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<ProfileDtoWithImage> updateProfile(ProfileDto profileDto, User user, Long id) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(user.getUserName(), user.getPassword());
            headers.setContentType(MediaType.APPLICATION_JSON);
            final HttpEntity<ProfileDto> requestEntity = new HttpEntity<>(profileDto, headers);
            return restTemplate.exchange(
                    restClientConfig.getProfileServiceUrl() + "/" + id,
                    HttpMethod.PUT,
                    requestEntity,
                    ProfileDtoWithImage.class
            );
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Profile service URL to update not found: {}", e.getMessage());
        } catch (HttpClientErrorException.BadRequest e) {
            log.error("Bad request to profile update service: {}", e.getMessage());
        } catch (HttpClientErrorException.Unauthorized e) {
            log.error("Unauthorized access to profile update service: {}", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private User getCurrentuser(Long telegramId) {
        final Optional<User> currentUser = userService.getUserByTelegramId(telegramId);
        if (currentUser.isEmpty()) {
            throw new RuntimeException("User not found for Telegram ID: " + telegramId);
        } else {
            return currentUser.get();
        }
    }
}


