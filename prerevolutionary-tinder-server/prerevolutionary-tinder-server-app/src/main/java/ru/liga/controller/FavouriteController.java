package ru.liga.controller;


import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.liga.dto.FavoriteDto;
import ru.liga.dto.FavoriteProfileDTO;
import ru.liga.model.Favorite;
import ru.liga.service.favourite.FavouriteService;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/favorite")
public class FavouriteController {

    private final FavouriteService favouriteService;
    private final ConversionService customConversionService;

    @GetMapping
    public ResponseEntity<Page<FavoriteProfileDTO>> getAll(@RequestParam() int page,
                                                           @RequestParam() int size) {
        final Page<FavoriteProfileDTO> matchingProfiles =
                favouriteService.findFavourites(PageRequest.of(page, size));
        if (matchingProfiles.hasContent()) {
            return new ResponseEntity<>(matchingProfiles, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteDto> getFavouriteById(@PathVariable Long id) {
        return favouriteService.getOne(id)
                .map(favourite -> new ResponseEntity<>(customConversionService.convert(
                        favourite, FavoriteDto.class), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{favoriteId}")
    public ResponseEntity<Favorite> addFavorite(@PathVariable Long favoriteId) {
        final Favorite favourite = favouriteService.createFavorite(favoriteId);
        return favourite != null ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.notFound().build();
    }

}
