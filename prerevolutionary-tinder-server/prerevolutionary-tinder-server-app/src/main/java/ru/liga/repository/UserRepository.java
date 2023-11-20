package ru.liga.repository;

import org.springframework.stereotype.Repository;
import ru.liga.model.User;

import java.util.Optional;

@Repository("userRepository")
/*@Transactional(timeout = 180)*/
public interface UserRepository extends BaseTinderServerRepository<User, Long> {

    Optional<User> findByUserName(String userName);
}
