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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.liga.dto.ProfileDto;
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

    private final RestClientConfig restClientConfig;
    private final RestTemplate restTemplate;
    private final UserService userService;

    @Override
    public Page<ProfileDto> findMatchingProfiles(Long telegramId, int page, int size) {
        try {
            final Optional<User> currentUser = userService.getUserByTelegramId(telegramId);
            if (currentUser.isEmpty()) {
                throw new RuntimeException("User not found for Telegram ID: " + telegramId);
            }
            final HttpHeaders headers = new HttpHeaders();
            currentUser.ifPresent(user -> headers.setBasicAuth(user.getUserName(), user.getPassword()));
            final UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(restClientConfig.getProfileServiceUrl())
                    .queryParam("page", page)
                    .queryParam("size", size);
            final ResponseEntity<CustomPageImpl<ProfileDto>> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<>() {
                    });
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            log.error("HTTP error during remote service call: {}", e.getRawStatusCode(), e);
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);
        } catch (RuntimeException e) {
            log.error("Error during remote service call", e);
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);
        }
    }
}


