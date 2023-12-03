package ru.liga.integration.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.liga.dto.UserDto;
import ru.liga.integration.config.RestClientConfig;
import ru.liga.model.ServiceUser;

import java.util.ResourceBundle;

import static ru.liga.utils.Base64EncoderDecoder.decode;

@Component
@Slf4j
@AllArgsConstructor
public class AuthApiImpl implements AuthApi {

    private final RestClientConfig restClientConfig;
    private final RestTemplate restTemplate;
    private final ResourceBundle resourceBundle;
    private final ServiceUser serviceUser;

    @Override
    public String remoteRegister(UserDto userDto) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(serviceUser.getUserName(), decode(serviceUser.getPassword()));
            headers.setContentType(MediaType.APPLICATION_JSON);
            final HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);
            restTemplate.postForEntity(restClientConfig.getRegisterServiceUrl(), requestEntity, UserDto.class);
            return HttpStatus.OK.name();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return resourceBundle.getString("registration.remote.service.error");
        }
    }
}
