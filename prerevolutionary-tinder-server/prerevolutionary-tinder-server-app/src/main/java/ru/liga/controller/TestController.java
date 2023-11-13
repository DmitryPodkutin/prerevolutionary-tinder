package ru.liga.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.liga.dto.filter.ProfileFilter;
import ru.liga.model.Profile;
import ru.liga.repository.ProfileRepository;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/rest")
/*@Transactional(timeout = 240)*/
public class TestController {

    private final ProfileRepository profileRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Profile> getAll(ProfileFilter filter, Pageable pageable) {
        return profileRepository.findAll();
    }

}
