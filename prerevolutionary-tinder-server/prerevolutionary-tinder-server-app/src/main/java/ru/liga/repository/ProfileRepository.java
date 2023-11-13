package ru.liga.repository;


import org.springframework.stereotype.Repository;
import ru.liga.model.Profile;

@Repository("profileRepository")
/*@Transactional(timeout = 180)*/
public interface ProfileRepository extends BaseTinderRepository<Profile, Long> {
}
