package ru.liga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.liga.model.PageInfo;

import java.util.Optional;

@Repository("matchingProfilesPageInfoRepository")
public interface MatchingProfilesPageInfoRepository extends JpaRepository<PageInfo, Long> {

    Optional<PageInfo> findByUserId(Long userId);

}
