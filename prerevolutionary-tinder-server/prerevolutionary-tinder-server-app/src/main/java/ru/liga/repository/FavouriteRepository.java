package ru.liga.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.liga.model.Favorite;

import java.util.List;

@Repository("favouriteRepository")
/*@Transactional(timeout = 180)*/
public interface FavouriteRepository extends BaseTinderRepository<Favorite, Long> {

    @Query(value = "SELECT * FROM public.favorite WHERE user_id = :userId", nativeQuery = true)
    List<Favorite> findAllByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM public.favorite WHERE user_id = :user ORDER BY :#{#sort.toString()}",
            nativeQuery = true)
    List<Favorite> findAllByUserAndFavoriteUser(@Param("user") Long user, @Param("sort") Sort sort);
}
