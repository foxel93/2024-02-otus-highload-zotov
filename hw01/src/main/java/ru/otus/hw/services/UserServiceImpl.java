package ru.otus.hw.services;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.UserRepository;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(long id) {
        return userRepository.findById(id).map(user -> {
            var userDto = new UserDto();

            userDto.setFirstName(user.getFirstName());
            userDto.setSecondName(user.getSecondName());
            userDto.setBirthDate(user.getBirthDate());
            userDto.setGender(user.getGender());
            userDto.setInterests(user.getInterests());
            userDto.setCity(user.getCity());

            return userDto;
        });
    }

    @Override
    @Transactional
    public boolean registerNewUserAccount(UserDto userDto) {
        if (userExists(userDto.getUsername())) {
            return false;
        }

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setSecondName(userDto.getSecondName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getUsername());
        user.setBirthDate(userDto.getBirthDate());
        user.setGender(userDto.getGender());
        user.setCity(userDto.getCity());
        user.setInterests(userDto.getInterests());

        return userRepository.save(user) != null;
    }

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));
    }

    private boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
