package ru.liga.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.liga.config.AppConfig;
import ru.liga.dto.UserDto;
import ru.liga.model.ServiceUser;
import ru.liga.model.User;
import ru.liga.model.UserState;
import ru.liga.telegrambot.model.StateType;

import java.util.ResourceBundle;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final AppConfig appConfig;
    private final ResourceBundle resourceBundle;
    private final ServiceUser serviceUser;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CredentialsValidator credentialsValidator;
    private final RestTemplate restTemplate;

    @Transactional
    public String registerUser(Long telegramId, String credentials) {
        final String[] credentialsParts = credentials.split(":");
        final String validateMessage = credentialsValidator.validate(credentialsParts);
        if (validateMessage == null) {
            final String userName = credentialsParts[0];
            final String password = passwordEncoder.encode(credentialsParts[1]);
            final UserDto userDto = new UserDto();
            userDto.setTelegramId(telegramId);
            userDto.setUserName(userName);
            userDto.setPassword(password);
            final String remoteRegisterMessage = remoteRegister(userDto);
            if ("OK".equals(remoteRegisterMessage)) {
                userService.createUser(new User(telegramId, userName, password,
                        new UserState(StateType.CREATE_PROFILE)));
            } else {
                return remoteRegisterMessage;
            }
        }
        return validateMessage;
    }

    public String remoteRegister(UserDto userDto) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(serviceUser.getUserName(), serviceUser.getPassword());
            headers.setContentType(MediaType.APPLICATION_JSON);
            final HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);
            restTemplate.postForEntity(appConfig.getRegisterServiceUrl(), requestEntity, UserDto.class);
            return HttpStatus.OK.name();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return resourceBundle.getString("registration.remote.service.error");
        }
    }
}
