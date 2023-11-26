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

@Component()
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream().peek(this::decodePassword).collect(Collectors.toList());
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId).map(this::decodePassword);
    }

    @Override
    public Optional<User> getUserByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId).map(this::decodePassword);
    }

    @Override
    public Optional<User> getUserByName(String userName) {
        return userRepository.findByUserName(userName).map(this::decodePassword);
    }

    @Override
    public Optional<User> createUser(User user) {
        if (user != null && user.getPassword() != null) {
            return Optional.of(userRepository.save(encodePassword(user)));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> updateUser(Long userId, User user) {
        return getUserById(userId)
                .map(existingUser -> {
                    existingUser.setUserName(user.getUserName());
                    userRepository.save(encodePassword(existingUser));
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
