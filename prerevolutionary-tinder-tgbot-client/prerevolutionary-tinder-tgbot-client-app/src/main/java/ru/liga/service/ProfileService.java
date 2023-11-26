package ru.liga.service;

import ru.liga.model.Profile;

import java.util.Optional;

public interface ProfileService {
    Profile saveProfile(Profile profile);

    Optional<Profile> getProfileById(Long id);

    Optional<Profile> getByChatId(Long chatId);
}
