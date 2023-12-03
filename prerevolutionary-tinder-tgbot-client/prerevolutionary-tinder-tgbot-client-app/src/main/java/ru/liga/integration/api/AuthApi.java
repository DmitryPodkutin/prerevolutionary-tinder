package ru.liga.integration.api;

import ru.liga.dto.UserDto;

/**
 * An interface representing an Authentication API for remote registration.
 */
public interface AuthApi {

    /**
     * Registers a user remotely via an authentication API.
     *
     * @param userDto The UserDto object containing user information to be registered.
     * @return A string representing the result or response from the remote registration.
     */
    String remoteRegister(UserDto userDto);
}
