package ru.liga.prerevolutionarytinderserver.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.prerevolutionarytinderserver.model.User;

@Repository("userRepository")
@Transactional(timeout = 180)
public interface UserRepository extends BaseTinderRepository<User,Long> {
}
