package ru.liga.integration.texttranslator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.liga.config.RestTemplateConfig;

import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class TranslationServiceImpl implements TranslationService {
    private final RestTemplate restTemplate;
    private final RestTemplateConfig restTemplateConfig;

    @Override
    public String translateToOldStyle(String text) {
        final String resourceUrl = restTemplateConfig.getRemoteServiceUrl() + text;
        try {
            final ResponseEntity<String> response = restTemplate.getForEntity(resourceUrl, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                log.error("Ошибка при вызове метода translate. Статус код: {}", response.getStatusCode());
                return "Ошибка при вызове метода translate";
            }
        } catch (HttpStatusCodeException e) {
            log.error("Ошибка HTTP при вызове метода translate: {}", e.getRawStatusCode(), e);
            throw new RuntimeException(e.getMessage());
        } catch (RestClientException e) {
            log.error("Ошибка RestClient при вызове метода translate", e);
            throw new RestClientException(Objects.requireNonNull(e.getMessage()));
        }
    }
}
