package ru.liga.service.user;

import ru.liga.model.AuthorizedUser;
import ru.liga.model.Profile;

import java.util.Optional;

public interface AuthenticationContext {

    Long getCurrentUserId();

    AuthorizedUser getCurrentUser();

    Optional<Profile> getCurrentUserProfile();
}
