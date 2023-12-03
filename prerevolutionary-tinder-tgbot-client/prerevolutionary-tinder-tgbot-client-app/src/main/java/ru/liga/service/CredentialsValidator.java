package ru.liga.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.model.User;

import java.util.ResourceBundle;


/**
 * Service responsible for validating user credentials.
 * This service performs validation on provided usernames and passwords during user registration.
 */
@Service
@AllArgsConstructor
public class CredentialsValidator {

    public static final String REGEX = "^[a-zA-Z0-9]+$";
    private final ResourceBundle resourceBundle;
    private final UserService userService;


    /**
     * Validates the provided credentials.
     *
     * @param credentialsParts The parts of the credentials to be validated (username and password).
     * @return A string indicating the validation status or any issues encountered during validation.
     */
    public String validate(String[] credentialsParts) {
        if (credentialsParts.length != 2) {
            return resourceBundle.getString("registration.invalid.format");
        }
        if (isValidUsername(credentialsParts[0]) != null) {
            return isValidUsername(credentialsParts[0]);
        } else if (isValidPassword(credentialsParts[1]) != null) {
            return isValidPassword(credentialsParts[1]);
        }
        return null;
    }

    private String isValidUsername(String userName) {
        if (!userName.matches(REGEX)) {
            return resourceBundle.getString("registration.invalid.user.name");
        }
        return isUserNameDuplicate(userName);
    }

    private String isValidPassword(String password) {
        if (!password.matches(REGEX)) {
            return resourceBundle.getString("registration.invalid.user.password");
        }
        return null;
    }

    private String isUserNameDuplicate(String userName) {
        final User user = userService.getUserByName(userName).orElse(null);
        if (user != null && userName.equals(user.getUserName())) {
            resourceBundle.getString("registration.duplicate.user.name");
        }
        return null;
    }
}

