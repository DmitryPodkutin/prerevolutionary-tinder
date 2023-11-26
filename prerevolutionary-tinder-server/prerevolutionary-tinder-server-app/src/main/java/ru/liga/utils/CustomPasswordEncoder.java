package ru.liga.utils;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.liga.dto.UserSaveDTO;
import ru.liga.model.User;

@Component
@AllArgsConstructor
public class CustomPasswordEncoder {

    private final PasswordEncoder passwordEncoder;

    public UserSaveDTO encodePassword(UserSaveDTO userSaveDTO) {
        if (userSaveDTO != null && userSaveDTO.getPassword() != null) {
            final String encoderPassword = passwordEncoder.encode(userSaveDTO.getPassword());
            userSaveDTO.setPassword(encoderPassword);
            return userSaveDTO;
        }
        return userSaveDTO;
    }

    public User encodePassword(User user) {
        if (user != null && user.getPassword() != null) {
            final String encoderPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encoderPassword);
            return user;
        }
        return user;
    }

}
