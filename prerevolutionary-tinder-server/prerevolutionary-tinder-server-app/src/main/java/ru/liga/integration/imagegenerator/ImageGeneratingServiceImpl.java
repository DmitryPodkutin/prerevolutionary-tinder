package ru.liga.integration.imagegenerator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.liga.config.RestTemplateConfig;

@Slf4j
@Component
@AllArgsConstructor
public class ImageGeneratingServiceImpl implements ImageGeneratingService {

    private final RestTemplate restTemplate;
    private final RestTemplateConfig restTemplateConfig;

    @Override
    public ResponseEntity<byte[]> fetchImageFromRemoteService(String resource) {
        final String resourceUrl = restTemplateConfig.getRemoteServiceUrl() + resource;

        try {
            final HttpHeaders headers = new HttpHeaders();
            final HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            final ResponseEntity<byte[]> response = restTemplate.exchange(
                    resourceUrl, HttpMethod.GET, requestEntity, byte[].class);

            if (response.getStatusCode().is2xxSuccessful()) {
                final HttpHeaders responseHeaders = response.getHeaders();
                responseHeaders.forEach((key, value) -> {
                    log.info("{} : {}", key, value);
                });

                return response;
            } else {
                log.error("Ошибка при получении изображения. Статус код: {}", response.getStatusCode());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (HttpStatusCodeException e) {
            log.error("Ошибка HTTP при получении изображения: {}", e.getRawStatusCode(), e);
            return ResponseEntity.status(e.getRawStatusCode()).build();
        } catch (RestClientException e) {
            log.error("Ошибка RestClient при получении изображения", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
