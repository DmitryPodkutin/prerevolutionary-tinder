package ru.liga.service;

import org.springframework.stereotype.Service;
import ru.liga.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Service interface to manage users within the application.
 */
@Service
public interface UserService {

    /**
     * Retrieves all users in the system.
     *
     * @return A list of all users.
     */
    List<User> getAllUsers();

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<User> getUserById(Long userId);

    /**
     * Retrieves a user by their Telegram ID.
     *
     * @param telegramId The Telegram ID of the user to retrieve.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<User> getUserByTelegramId(Long telegramId);

    /**
     * Retrieves a user by their username.
     *
     * @param userName The username of the user to retrieve.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<User> getUserByName(String userName);

    /**
     * Creates a new user.
     *
     * @param user The user to create.
     * @return An Optional containing the created user if successful, otherwise empty.
     */
    Optional<User> createUser(User user);


    /**
     * Updates an existing user.
     *
     * @param userId The ID of the user to update.
     * @param user   The updated user information.
     * @return An Optional containing the updated user if successful, otherwise empty.
     */
    Optional<User> updateUser(Long userId, User user);

    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to delete.
     * @return A boolean indicating whether the user was successfully deleted or not.
     */
    boolean deleteUser(Long userId);
}
