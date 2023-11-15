package ru.liga.service.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.liga.model.AuthorizedUser;

@Service
public class AuthenticationContextImpl implements AuthenticationContext {

    @Override
    public Long getCurrentUserId() {
        final AuthorizedUser currentUser = getCurrentUser();
        return (currentUser != null) ? currentUser.getUserId() : null;
    }

    @Override
    public AuthorizedUser getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof AuthorizedUser) {
            return (AuthorizedUser) authentication.getPrincipal();
        }
        return null;
    }
}
