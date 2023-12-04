package ru.liga.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.liga.model.UserState;

import java.util.Optional;

@Repository("userStateRepository")
public interface UserStateRepository extends BaseTinderClientRepository<UserState, Long> {

    @Query(value = "select * from public.user_state us where user_id  = :userId", nativeQuery = true)
    Optional<UserState> findByUserId(Long userId);
}
