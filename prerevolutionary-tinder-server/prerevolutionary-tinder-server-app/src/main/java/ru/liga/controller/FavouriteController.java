package ru.liga.controller;


import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.liga.dto.FavoriteDto;
import ru.liga.dto.filter.FavouriteFilter;
import ru.liga.service.favourite.FavouriteService;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/favourite")
/*@Transactional(timeout = 240)*/
public class FavouriteController {

    private final FavouriteService favouriteService;
    private final ConversionService customConversionService;

    @GetMapping
    public ResponseEntity<List<FavoriteDto>> getAll(FavouriteFilter filter, Pageable pageable) {
        final List<FavoriteDto> favourites = favouriteService.findFavouritesByFilter(filter).stream()
                .map(favourite -> customConversionService.convert(favourite, FavoriteDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(favourites, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteDto> getFavouriteById(@PathVariable Long id) {
        return favouriteService.getOne(id)
                .map(favourite -> new ResponseEntity<>(customConversionService.convert(
                        favourite, FavoriteDto.class), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //TODO - здесб должен быть метод по добавлению в любимки


}
