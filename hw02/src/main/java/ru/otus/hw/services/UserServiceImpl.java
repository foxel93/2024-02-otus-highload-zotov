package ru.otus.hw.services;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserSearchDto;
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
        return userRepository.findById(id).map(this::toDto);
    }

    @Override
    public List<UserDto> findByNameAndSurnamePrefix(UserSearchDto userSearchDto) {
        return userRepository.findByNameAndSurnamePrefix(userSearchDto.getFirstName(), userSearchDto.getSecondName())
            .stream()
            .sorted(Comparator.comparingLong(User::getId))
            .map(this::toDto)
            .toList();
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

    private UserDto toDto(User user) {
        var userDto = new UserDto();

        userDto.setFirstName(user.getFirstName());
        userDto.setSecondName(user.getSecondName());
        userDto.setBirthDate(user.getBirthDate());
        userDto.setGender(user.getGender());
        userDto.setInterests(user.getInterests());
        userDto.setCity(user.getCity());

        return userDto;
    }
}
