package ru.liga.service.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.liga.enums.Role;
import ru.liga.model.AuthorizedUser;
import ru.liga.model.Profile;
import ru.liga.model.User;
import ru.liga.model.UserRole;
import ru.liga.repository.UserRepository;
import ru.liga.service.profile.ProfileService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final ProfileService profileService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUserName(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User name:%s not found", username)));
        final Profile profile = profileService.getByUserId(user.getId())
                .orElse(null);
        return new AuthorizedUser(user, getGrantedAuthorities(user.getUserRoles()), profile);
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(Collection<UserRole> userRoles) {
        return userRoles.stream().map(role -> new SimpleGrantedAuthority(Role.USER.name()))
                .collect(Collectors.toList());
    }
}
