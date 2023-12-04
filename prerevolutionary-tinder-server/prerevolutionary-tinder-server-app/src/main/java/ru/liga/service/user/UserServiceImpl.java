package ru.liga.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.dto.UserSaveDTO;
import ru.liga.model.User;
import ru.liga.repository.UserRepository;
import ru.liga.utils.CustomPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomPasswordEncoder customPasswordEncoder;
    private final ResourceBundle logMessages;

    @Override
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            log.error(logMessages.getString("error.retrieving.all.users"), e);
            throw e;
        }
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        try {
            return userRepository.findById(userId);
        } catch (Exception e) {
            log.error(logMessages.getString("error.retrieving.user.by.id"), userId, e);
            throw e;
        }
    }

    @Override
    public Optional<User> getUserByUserName(String userName) {
        try {
            return userRepository.findByUserName(userName);
        } catch (Exception e) {
            log.error(logMessages.getString("error.retrieving.user.by.username"), userName, e);
            throw e;
        }
    }

    @Override
    public Optional<User> createUser(User user) {
        try {
            return Optional.of(userRepository.save(customPasswordEncoder.encodePassword(user)));
        } catch (Exception e) {
            log.error(logMessages.getString("error.creating.user"), e);
            throw e;
        }
    }

    @Override
    public Optional<User> registration(UserSaveDTO userSaveDTO) {
        try {
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
        } catch (Exception e) {
            log.error(logMessages.getString("error.during.user.registration"), userSaveDTO.getUserName(), e);
            throw e;
        }
    }

    @Override
    public Optional<User> updateUser(Long userId, User user) {
        try {
            return getUserById(userId)
                    .map(existingUser -> {
                        existingUser.setUserName(user.getUserName());
                        existingUser.setPassword(user.getPassword());
                        customPasswordEncoder.encodePassword(existingUser);
                        userRepository.save(existingUser);
                        return existingUser;
                    });
        } catch (Exception e) {
            log.error(logMessages.getString("error.updating.user"), userId, e);
            throw e;
        }
    }

    @Override
    public boolean deleteUser(Long userId) {
        try {
            return getUserById(userId)
                    .map(user -> {
                        userRepository.deleteById(userId);
                        return true;
                    }).orElse(false);
        } catch (Exception e) {
            log.error(logMessages.getString("error.deleting.user"), userId, e);
            throw e;
        }
    }
}
