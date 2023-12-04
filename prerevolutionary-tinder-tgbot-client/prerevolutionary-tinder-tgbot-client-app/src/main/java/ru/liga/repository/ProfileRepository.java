package ru.liga.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.liga.model.Profile;

import java.util.Optional;

@Repository("profileRepository")
public interface ProfileRepository extends BaseTinderClientRepository<Profile, Long> {

    @Query(value = "select * from public.profile where chat_id = :chatId", nativeQuery = true)
    Optional<Profile> findByChatId(Long chatId);
}
