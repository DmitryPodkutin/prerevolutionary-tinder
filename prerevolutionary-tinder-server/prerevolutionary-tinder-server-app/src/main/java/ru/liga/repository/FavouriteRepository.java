package ru.liga.repository;


import org.springframework.stereotype.Repository;
import ru.liga.model.Favorite;

@Repository("favouriteRepository")
/*@Transactional(timeout = 180)*/
public interface FavouriteRepository extends BaseTinderRepository<Favorite, Long> {
}
