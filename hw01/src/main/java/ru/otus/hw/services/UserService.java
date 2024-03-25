package ru.otus.hw.services;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.hw.dto.UserDto;

public interface UserService extends UserDetailsService {
    Optional<UserDto> findById(long id);

    boolean registerNewUserAccount(UserDto userDto);
}
