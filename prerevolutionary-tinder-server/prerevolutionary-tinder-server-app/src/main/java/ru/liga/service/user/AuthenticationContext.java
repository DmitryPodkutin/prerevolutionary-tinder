package ru.liga.service.user;

import ru.liga.model.AuthorizedUser;

public interface AuthenticationContext {
    Long getCurrentUserId();

    AuthorizedUser getCurrentUser();
}
