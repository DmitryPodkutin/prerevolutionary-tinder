package ru.liga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.liga.model.MatchingProfilesPageInfo;

import java.util.Optional;

@Repository("matchingProfilesPageInfoRepository")
public interface MatchingProfilesPageInfoRepository extends JpaRepository<MatchingProfilesPageInfo, Long> {

    Optional<MatchingProfilesPageInfo> findByUserId(Long userId);

}
