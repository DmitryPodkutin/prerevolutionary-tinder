package ru.liga.service.user;

import org.springframework.stereotype.Service;
import ru.liga.model.User;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    List<User> getAllUsers();

    Optional<User> getUserById(Long userId);

    Optional<User> getByteTelegramUserId(Long telegramUserId);

    Optional<User> getUserByUserName(String userName);

    Optional<User> createUser(User user);

    Optional<User> registration(User user);

    Optional<User> updateUser(Long userId, User user);

    boolean deleteUser(Long userId);
}
