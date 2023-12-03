package ru.liga.integration.service;

/**
 * Interface for user registration service.
 * Implementations of this service can be used to register users.
 */
public interface RegistrationService {

    /**
     * Registers a user with the provided Telegram ID and credentials.
     *
     * @param telegramId  The Telegram ID of the user to be registered.
     * @param credentials The credentials associated with the user.
     * @return A string representing the registration status or information.
     */
    String registerUser(Long telegramId, String credentials);
}
