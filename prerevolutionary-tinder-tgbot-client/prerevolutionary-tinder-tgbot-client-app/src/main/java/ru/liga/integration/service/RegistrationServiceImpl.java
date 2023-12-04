package ru.liga.integration.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.dto.UserDto;
import ru.liga.integration.api.AuthApi;
import ru.liga.model.PageInfo;
import ru.liga.model.User;
import ru.liga.model.UserState;
import ru.liga.service.CredentialsValidator;
import ru.liga.service.UserService;
import ru.liga.telegrambot.model.StateType;

/**
 * Service implementation for user registration.
 * This service allows users to be registered and stored in the system.
 */
@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserService userService;
    private final CredentialsValidator credentialsValidator;
    private final AuthApi authApi;

    /**
     * Registers a user with the provided Telegram ID and credentials.
     *
     * @param telegramId   The Telegram ID of the user to be registered.
     * @param credentials  The credentials associated with the user (format: username:password).
     * @return A string indicating the registration status or information.
     */
    @Override
    @Transactional
    public String registerUser(Long telegramId, String credentials) {
        final String[] credentialsParts = credentials.split(":");
        final String validateMessage = credentialsValidator.validate(credentialsParts);
        if (validateMessage == null) {
            final String userName = credentialsParts[0];
            final String password = credentialsParts[1];
            final UserDto userDto = new UserDto();
            userDto.setTelegramId(telegramId);
            userDto.setUserName(userName);
            userDto.setPassword(password);
            final String remoteRegisterMessage = authApi.remoteRegister(userDto);
            if ("OK".equals(remoteRegisterMessage)) {
                userService.createUser(new User(telegramId, userName, password,
                        new UserState(StateType.CREATE_PROFILE),
                        new PageInfo(0, 0)));
            } else {
                return remoteRegisterMessage;
            }
        }
        return validateMessage;
    }
}
