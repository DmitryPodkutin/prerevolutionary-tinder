package ru.liga.integration.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.liga.dto.ProfileDto;
import ru.liga.integration.config.RestClientConfig;
import ru.liga.model.User;
import ru.liga.service.UserService;

import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class ProfileApiImpl implements ProfileApi {

    private final RestClientConfig restClientConfig;
    private final RestTemplate restTemplate;
    private final UserService userService;

    @Override
    public ProfileDto findMatchingProfiles(Long telegramId) {
        try {
            final Optional<User> currentUser = userService.getUserByTelegramId(telegramId);
            if (currentUser.isEmpty()) {
                throw new RuntimeException("User not found for Telegram ID: " + telegramId);
            }
            final HttpHeaders headers = new HttpHeaders();
            currentUser.ifPresent(user -> headers.setBasicAuth(user.getUserName(), user.getPassword()));
            final ResponseEntity<ProfileDto> responseEntity = restTemplate.exchange(
                    restClientConfig.getRegisterServiceUrl(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    ProfileDto.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            log.error("HTTP error during remote service call: {}", e.getRawStatusCode(), e);
            return null;
        } catch (RuntimeException e) {
            log.error("Error during remote service call", e);
            return null;
        }
    }
}


