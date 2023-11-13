package ru.liga.service.favourite;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.liga.dto.filter.FavouriteFilter;
import ru.liga.model.Favorite;
import ru.liga.repository.FavouriteRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FavouriteServiceImpl implements FavouriteService {

    private final FavouriteRepository favouriteRepository;

    public List<Favorite> findFavouritesByFilter(FavouriteFilter filter) {
        return favouriteRepository.findAllByUserAndFavoriteUser(filter.getUser().getId(), getSort(filter));
    }

    @Override
    public Optional<Favorite> getOne(Long id) {
        return favouriteRepository.findById(id);
    }

    private Sort getSort(FavouriteFilter filter) {
        final Sort sort = Sort.by(filter.getSortBy().stream()
                .map(customSort -> new Sort.Order(customSort.getDirection(), customSort.getColumnName()))
                .collect(Collectors.toList()));
        return sort;
    }
}
