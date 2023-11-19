package ru.liga.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.model.User;
import ru.liga.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Component()
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    @Override
    public Optional<User> createUser(User user) {
        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> updateUser(Long userId, User user) {
        return getUserById(userId)
                .map(existingUser -> {
                    existingUser.setUserName(user.getUserName());
                    existingUser.setPassword(user.getPassword());
                    userRepository.save(existingUser);
                    return existingUser;
                });
    }

    @Override
    public boolean deleteUser(Long userId) {
        return getUserById(userId)
                .map(user -> {
                    userRepository.deleteById(userId);
                    return true;
                }).orElse(false);
    }
}
