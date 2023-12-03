package ru.liga.integration.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.liga.dto.FavoriteDto;
import ru.liga.integration.config.RestClientConfig;
import ru.liga.model.User;

import java.util.ResourceBundle;

@Slf4j
@Component
@AllArgsConstructor
public class FavoriteApiImpl implements FavoriteApi {

    private final RestTemplate restTemplate;
    private final RestClientConfig restClientConfig;
    private final ResourceBundle logMessages;

    @Override
    public ResponseEntity<FavoriteDto> addFavorite(Long favoriteId, User currentUser) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(currentUser.getUserName(), currentUser.getPassword());
            headers.setContentType(MediaType.APPLICATION_JSON);
            final HttpEntity<FavoriteDto> requestEntity = new HttpEntity<>(null, headers);

            restTemplate.postForEntity(
                    restClientConfig.getFavoriteServiceUrl() +
                            String.format("/%d", favoriteId), requestEntity, FavoriteDto.class);
            log.info(logMessages.getString("success.favorite.creation"));
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (HttpClientErrorException.NotFound e) {
            log.error(logMessages.getString("error.profile.service.not.found"), e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (HttpClientErrorException.BadRequest e) {
            log.error(logMessages.getString("error.bad.request.to.profile.create.service"), e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (HttpClientErrorException.Unauthorized e) {
            log.error(logMessages.getString("error.unauthorized.access.to.profile.create.service"),
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (HttpClientErrorException.Forbidden e) {
            log.error(logMessages.getString("error.access.to.profile.create.service.forbidden"), e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            log.error(logMessages.getString("error.internal.server.error"), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
