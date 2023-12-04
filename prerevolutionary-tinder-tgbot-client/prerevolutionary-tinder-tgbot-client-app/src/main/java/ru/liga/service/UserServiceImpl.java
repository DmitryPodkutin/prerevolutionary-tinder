package ru.liga.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.model.User;
import ru.liga.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.liga.utils.Base64EncoderDecoder.decode;
import static ru.liga.utils.Base64EncoderDecoder.encode;

/**
 * Implementation of the UserService interface responsible for managing user-related operations.
 */
@Component()
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Retrieves all users in the system.
     *
     * @return A list of all users.
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream().peek(this::decodePassword).collect(Collectors.toList());
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return An Optional containing the user if found, otherwise empty.
     */
    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId).map(this::decodePassword);
    }

    /**
     * Retrieves a user by their Telegram ID.
     *
     * @param telegramId The Telegram ID of the user to retrieve.
     * @return An Optional containing the user if found, otherwise empty.
     */
    @Override
    public Optional<User> getUserByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId).map(this::decodePassword);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param userName The username of the user to retrieve.
     * @return An Optional containing the user if found, otherwise empty.
     */
    @Override
    public Optional<User> getUserByName(String userName) {
        return userRepository.findByUserName(userName).map(this::decodePassword);
    }

    /**
     * Creates a new user.
     *
     * @param user The user to create.
     * @return An Optional containing the created user if successful, otherwise empty.
     */
    @Override
    public Optional<User> createUser(User user) {
        if (user != null && user.getPassword() != null) {
            return Optional.of(userRepository.save(encodePassword(user)));
        }
        return Optional.empty();
    }

    /**
     * Updates an existing user.
     *
     * @param userId The ID of the user to update.
     * @param user   The updated user information.
     * @return An Optional containing the updated user if successful, otherwise empty.
     */
    @Override
    public Optional<User> updateUser(Long userId, User user) {
        return getUserById(userId)
                .map(existingUser -> {
                    existingUser.setUserName(user.getUserName());
                    userRepository.save(encodePassword(existingUser));
                    return existingUser;
                });
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to delete.
     * @return A boolean indicating whether the user was successfully deleted or not.
     */
    @Override
    public boolean deleteUser(Long userId) {
        return getUserById(userId)
                .map(user -> {
                    userRepository.deleteById(userId);
                    return true;
                }).orElse(false);
    }

    private User encodePassword(User user) {
        final String encodePassword = encode(user.getPassword());
        user.setPassword(encodePassword);
        return user;
    }

    private User decodePassword(User user) {
        final String decodePassword = decode(user.getPassword());
        user.setPassword(decodePassword);
        return user;
    }
}
