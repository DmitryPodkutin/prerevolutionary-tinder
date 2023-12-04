package ru.liga.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.liga.model.Favorite;
import ru.liga.model.User;

import java.util.List;

@Repository("favouriteRepository")
/*@Transactional(timeout = 180)*/
public interface FavouriteRepository extends BaseTinderServerRepository<Favorite, Long> {

    @Query(value = "SELECT * FROM public.favorite WHERE user_id = :userId", nativeQuery = true)
    List<Favorite> findAllByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM public.favorite WHERE user_id = :currentUserId ORDER BY :#{#sort.toString()}",
            nativeQuery = true)
    List<Favorite> findAllByUserAndFavoriteUser(@Param("currentUserId") Long currentUserId, @Param("sort") Sort sort);

    @Query(value = "SELECT * FROM public.favorite WHERE favorite_user_id = :currentUserId",
            nativeQuery = true)
    List<Favorite> findAllFavoritesByFavoritesUserId(Long currentUserId);

    @Query(value = "SELECT f FROM Favorite f " +
            "WHERE f.favoriteUser = :currentUser " +
            "OR f.user = :currentUser ")
    Page<Favorite> findMatchingProfiles(User currentUser, Pageable pageable);
}
