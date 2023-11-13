package ru.liga.service.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.liga.enums.Role;
import ru.liga.model.AuthorizedUser;
import ru.liga.model.Profile;
import ru.liga.model.User;
import ru.liga.model.UserRole;
import ru.liga.repository.UserRepository;
import ru.liga.service.profile.ProfileService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component()
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final ProfileService profileService;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getByteTelegramUserId(Long telegramUserId) {
        return userRepository.findByTelegramUserId(telegramUserId);
    }

    @Override
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public Optional<User> createUser(User user) {
        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> registration(User user) {
        return userRepository.findByUserName(user.getUserName())
                .map(existingUser -> Optional.<User>empty())
                .orElseGet(() -> {
                    userRepository.save(user);
                    return Optional.of(user);
                });
    }

    @Override
    public Optional<User> updateUser(Long userId, User user) {
        return getUserById(userId)
                .map(existingUser -> {
                    existingUser.setUserName(user.getUserName());
                    existingUser.setPassword(user.getPassword());
                    userRepository.save(existingUser);
                    return existingUser;
                });
    }

    public boolean deleteUser(Long userId) {
        return getUserById(userId)
                .map(user -> {
                    userRepository.deleteById(userId);
                    return true;
                }).orElse(false);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = getUserByUserName(username).orElseThrow(
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
