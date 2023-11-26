package ru.liga.integration.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.liga.config.AppConfig;
import ru.liga.dto.ProfileDto;
import ru.liga.model.User;

@Slf4j
@Service
@AllArgsConstructor
public class ProfileClientServiceImpl implements ProfileClientService {
    private final RestTemplate restTemplate;
    private final AppConfig appConfig;

    @Override
    public ProfileDto findMatchingProfiles(Long telegramId) {
        return null;
    }

    @Override
    public void createProfile(ProfileDto profileDto, User user) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(user.getUserName(), user.getPassword());
            headers.setContentType(MediaType.APPLICATION_JSON);
            final HttpEntity<ProfileDto> requestEntity = new HttpEntity<>(profileDto, headers);
            restTemplate.postForEntity(appConfig.getProfileUrl(), requestEntity, ProfileDto.class);
        } catch (Exception e) {
            log.error("Error while creating profile: {}", e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> getProfile(User user) {
        ResponseEntity<String> profileResponse = null;
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(user.getUserName(), user.getPassword());
            headers.setContentType(MediaType.APPLICATION_JSON);
            profileResponse = restTemplate.getForEntity(appConfig.getProfileUrl(), null, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Profile not found for URL: {}", appConfig.getProfileUrl());
        }
        return profileResponse;
    }
}

