package ru.liga.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class AuthorizedUser extends org.springframework.security.core.userdetails.User {

    private final Long userId;
    private final Profile profile;

    public AuthorizedUser(User user, Collection<? extends GrantedAuthority> authorities, Profile profile) {
        super(user.getUserName(), user.getPassword(), authorities);
        this.userId = user.getId();
        this.profile = profile;
    }
}
