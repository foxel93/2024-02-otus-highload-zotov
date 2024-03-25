package ru.otus.hw.repositories;

import java.util.Optional;
import ru.otus.hw.models.User;

public interface UserRepository {
    Optional<User> findById(long id);

    Optional<User> findByUsername(String username);

    User save(User user);

    void deleteById(long id);
}
