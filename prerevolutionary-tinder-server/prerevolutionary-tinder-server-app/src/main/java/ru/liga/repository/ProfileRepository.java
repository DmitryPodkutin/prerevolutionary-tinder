package ru.liga.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.liga.enums.Gender;
import ru.liga.enums.SeekingFor;
import ru.liga.model.Profile;

import java.util.List;
import java.util.Optional;

@Repository("profileRepository")
/*@Transactional(timeout = 180)*/
public interface ProfileRepository extends BaseTinderServerRepository<Profile, Long> {

    Optional<Profile> findByUserId(Long userId);

    @Query("SELECT p FROM Profile p WHERE p.gender = :currentUserGender " +
            "AND (p.seeking = :currentUserSeeking OR p.seeking = 'ALL')")
    List<Profile> findMatchingProfiles(@Param("currentUserGender") Gender currentUserGender,
                                           @Param("currentUserSeeking") SeekingFor currentUserSeeking);

}

