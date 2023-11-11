package ru.liga.prerevolutionarytinderserver.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.liga.prerevolutionarytinderserver.dto.filter.ProfileFilter;
import ru.liga.prerevolutionarytinderserver.model.Profile;
import ru.liga.prerevolutionarytinderserver.repository.ProfileRepository;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/rest")
@Transactional(timeout = 240)
public class TestController {

    private final ProfileRepository profileRepository;

    @Autowired
    public TestController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Profile> getAll (ProfileFilter filter, Pageable pageable) {
        return profileRepository.findAll()  ;
    }

}
