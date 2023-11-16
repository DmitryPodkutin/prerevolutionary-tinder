package ru.liga.service.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.liga.model.AuthorizedUser;
import ru.liga.model.Profile;

import java.util.Optional;

@Service
public class AuthenticationContextImpl implements AuthenticationContext {

    @Override
    public Long getCurrentUserId() {
        final AuthorizedUser currentUser = getCurrentUser();
        return (currentUser != null) ? currentUser.getUserId() : null;
    }

    @Override
    public AuthorizedUser getCurrentUser() { //TODO Перевести на Optional
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof AuthorizedUser) {
            return (AuthorizedUser) authentication.getPrincipal();
        }
        return null;
    }

    @Override
    public Optional<Profile> getCurrentUserProfile() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof AuthorizedUser) {
            return Optional.ofNullable(((AuthorizedUser) authentication.getPrincipal()).getProfile());
        }
        return Optional.empty();
    }
}
