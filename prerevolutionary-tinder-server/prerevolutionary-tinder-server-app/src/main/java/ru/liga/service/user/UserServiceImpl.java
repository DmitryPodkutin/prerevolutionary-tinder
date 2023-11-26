package ru.liga.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.dto.UserSaveDTO;
import ru.liga.model.User;
import ru.liga.repository.UserRepository;
import ru.liga.utils.CustomPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Component()
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomPasswordEncoder customPasswordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public Optional<User> createUser(User user) {
        return Optional.of(userRepository.save(customPasswordEncoder.encodePassword(user)));
    }

    @Override
    public Optional<User> registration(UserSaveDTO userSaveDTO) {
        final Optional<User> byUserName = userRepository.findByUserName(userSaveDTO.getUserName());
        if (byUserName.isPresent()) {
            return byUserName;
        } else {
            final User user = new User();
            user.setUserName(userSaveDTO.getUserName());
            user.setPassword(userSaveDTO.getPassword());
            customPasswordEncoder.encodePassword(user);
            userRepository.save(user);
            return Optional.of(user);
        }
    }

    @Override
    public Optional<User> updateUser(Long userId, User user) {
        return getUserById(userId)
                .map(existingUser -> {
                    existingUser.setUserName(user.getUserName());
                    existingUser.setPassword(user.getPassword());
                    customPasswordEncoder.encodePassword(existingUser);
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
