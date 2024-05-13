package ru.otus.hw.services;

import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserSearchDto;

public interface UserService extends UserDetailsService {
    Optional<UserDto> findById(long id);

    List<UserDto> findByNameAndSurnamePrefix(UserSearchDto userSearchDto);

    boolean registerNewUserAccount(UserDto userDto);
}
