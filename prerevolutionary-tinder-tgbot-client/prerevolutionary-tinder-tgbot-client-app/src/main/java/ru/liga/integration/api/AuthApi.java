package ru.liga.integration.api;

import ru.liga.dto.UserDto;

public interface AuthApi {
    String remoteRegister(UserDto userDto);
}
