package ru.otus.hw.repositories;

import java.util.List;
import java.util.Optional;
import ru.otus.hw.models.User;

public interface UserRepository {
    Optional<User> findById(long id);

    Optional<User> findByUsername(String username);

    List<User> findByNameAndSurnamePrefix(String firstName, String secondName);

    User save(User user);

    List<User> saveAll(List<User> users);

    void deleteById(long id);
}
