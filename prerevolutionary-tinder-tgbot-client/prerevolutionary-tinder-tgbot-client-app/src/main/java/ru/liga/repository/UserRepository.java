package ru.liga.repository;

import org.springframework.stereotype.Repository;
import ru.liga.model.User;

import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends BaseTinderClientRepository<User, Long> {

    Optional<User> findByTelegramId(Long telegramId);

    Optional<User> findByUserName(String userName);


}
