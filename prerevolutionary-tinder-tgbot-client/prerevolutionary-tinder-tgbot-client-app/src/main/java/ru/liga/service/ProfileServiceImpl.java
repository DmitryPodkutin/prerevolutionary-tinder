package ru.liga.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.model.Profile;
import ru.liga.repository.ProfileRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;


    @Override
    public Profile saveProfile(Profile profile) {
        return profileRepository.saveAndFlush(profile);
    }

    @Override
    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    @Override
    public Optional<Profile> getByChatId(Long chatId) {
        return profileRepository.findByChatId(chatId);
    }

    @Override
    public void deleteTempProfile(Profile profile) {
        profileRepository.delete(profile);
    }
}
