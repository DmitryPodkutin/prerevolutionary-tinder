package ru.liga.service;

import org.springframework.stereotype.Service;
import ru.liga.model.User;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    List<User> getAllUsers();

    Optional<User> getUserById(Long userId);

    Optional<User> getUserByTelegramId(Long telegramId);

    Optional<User> createUser(User user);

    Optional<User> updateUser(Long userId, User user);

    boolean deleteUser(Long userId);
}
