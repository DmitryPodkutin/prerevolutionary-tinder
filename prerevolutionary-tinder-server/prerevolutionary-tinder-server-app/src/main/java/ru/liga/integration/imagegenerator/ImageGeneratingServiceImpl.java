package ru.liga.integration.imagegenerator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.liga.config.RestTemplateConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
@AllArgsConstructor
public class ImageGeneratingServiceImpl implements ImageGeneratingService {

    private final RestTemplate restTemplate;
    private final RestTemplateConfig restTemplateConfig;

    @Override
    public byte[] getProfileImage(String resource) {
        final ResponseEntity<Resource> imageGeneratorResponse = fetchImageFromRemoteService(resource);
        return extractImageData(imageGeneratorResponse);
    }

    private ResponseEntity<Resource> fetchImageFromRemoteService(String resource) {
        final String resourceUrl = restTemplateConfig.getImageGeneratorServiceUrl() + resource;

        try {
            final HttpHeaders headers = new HttpHeaders();
            final HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            final ResponseEntity<byte[]> response = restTemplate.exchange(
                    resourceUrl, HttpMethod.GET, requestEntity, byte[].class);

            if (response.getStatusCode().is2xxSuccessful()) {
                final ByteArrayResource resourceBody = new ByteArrayResource(response.getBody());

                final HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.setContentDisposition(
                        ContentDisposition.parse("attachment; filename=questionnaire.png"));
                responseHeaders.setContentType(MediaType.IMAGE_PNG); // Устанавливаем Content-Type для PNG-изображений

                return new ResponseEntity<>(resourceBody, responseHeaders, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (HttpClientErrorException e) {
            log.error("HTTP error while fetching the image: {}", e.getRawStatusCode(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RestClientException e) {
            log.error("RestClient error while fetching the image", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public byte[] extractImageData(ResponseEntity<Resource> responseEntity) {
        final HttpHeaders headers = responseEntity.getHeaders();
        final Resource resource = responseEntity.getBody();

        if (resource != null) {
            final MediaType contentType = headers.getContentType();
            if (contentType != null && contentType.isCompatibleWith(MediaType.IMAGE_PNG)) {
                try {
                    final InputStream inputStream = resource.getInputStream();
                    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    final byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    return outputStream.toByteArray();
                } catch (IOException e) {
                    log.error("Error reading image data from the resource", e);
                }
            } else {
                log.warn("Incompatible content type or not an image PNG");
            }
        } else {
            log.error("Error extracting resource");
        }
        return null;
    }

}
