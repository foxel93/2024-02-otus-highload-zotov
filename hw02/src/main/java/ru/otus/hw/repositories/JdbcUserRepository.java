package ru.otus.hw.repositories;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Gender;
import ru.otus.hw.models.User;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private static final RowMapper<User> MAPPER = (rs, rowNum) -> mapUserRow(rs);

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<User> findById(long id) {
        var sqlQuery = """
                SELECT id, username, password, firstName, secondName, birthDate, gender, interests, city
                FROM users
                WHERE id = :id
            """;
        return jdbc.query(sqlQuery, mapUserIdParam(id), new UserResultSetExtractor());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        var sqlQuery = """
                SELECT id, username, password, firstName, secondName, birthDate, gender, interests, city
                FROM users
                WHERE username = :username
            """;
        return jdbc.query(sqlQuery, mapUsernameParam(username), new UserResultSetExtractor());
    }

    @Override
    public List<User> findByNameAndSurnamePrefix(String firstName, String secondName) {
        var sqlQuery = """
                SELECT id, username, password, firstName, secondName, birthDate, gender, interests, city
                FROM users
                WHERE firstName LIKE :firstName and secondName LIKE :secondName
            """;
        return jdbc.query(sqlQuery, mapNamePrefixParam(firstName, secondName), MAPPER);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            return insert(user);
        }
        return update(user);
    }

    @Override
    public void deleteById(long id) {
        var sqlQuery = """
                DELETE FROM users
                WHERE  id = :id
            """;
        jdbc.update(sqlQuery, mapUserIdParam(id));
    }

    @RequiredArgsConstructor
    private static class UserResultSetExtractor implements ResultSetExtractor<Optional<User>> {
        @Override
        public Optional<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            User user = null;
            while (rs.next()) {
                if (user == null) {
                    user = mapUserRow(rs);
                }
            }
            return Optional.ofNullable(user);
        }
    }

    private User insert(User user) {
        var keyHolder = new GeneratedKeyHolder();
        var sqlQuery = """
                INSERT INTO users (username, password, firstName, secondName, birthDate, gender, interests, city)
                VALUES (:username, :password, :firstName, :secondName, :birthDate, :gender, :interests, :city)
            """;
        jdbc.update(sqlQuery, mapUserParams(user), keyHolder, new String[] {"id"});

        user.setId(keyHolder.getKeyAs(Long.class));
        return user;
    }

    private User update(User user) {
        var sqlQuery = """
                UPDATE users
                SET
                    username = :username,
                    password = :password,
                    firstName = :firstName,
                    secondName = :secondName,
                    birthDate = :birthDate,
                    gender = :gender,
                    interests = :interests,
                    city = :city
                WHERE id = :id
            """;
        var result = jdbc.update(sqlQuery, mapUserParams(user));

        if (result == 0) {
            throw new EntityNotFoundException("User with id %d not found".formatted(user.getId()));
        }
        return user;
    }

    private static MapSqlParameterSource mapUserIdParam(long userId) {
        var params = new MapSqlParameterSource();
        params.addValue("id", userId);
        return params;
    }

    private static MapSqlParameterSource mapUsernameParam(String username) {
        var params = new MapSqlParameterSource();
        params.addValue("username", username);
        return params;
    }

    private static MapSqlParameterSource mapNamePrefixParam(String firstName, String secondName) {
        var params = new MapSqlParameterSource();
        params.addValue("firstName", firstName + "%");
        params.addValue("secondName", secondName + "%");
        return params;
    }

    private static MapSqlParameterSource mapUserParams(User user) {
        var params = new MapSqlParameterSource();
        params.addValue("id", user.getId());
        params.addValue("username", user.getUsername());
        params.addValue("password", user.getPassword());
        params.addValue("firstName", user.getFirstName());
        params.addValue("secondName", user.getSecondName());
        params.addValue("birthDate", user.getBirthDate());
        params.addValue("gender", user.getGender().name());
        params.addValue("interests", user.getInterests());
        params.addValue("city", user.getCity());
        return params;
    }

    private static User mapUserRow(ResultSet rs) throws SQLException {
        return new User(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("firstName"),
            rs.getString("secondName"),
            Date.valueOf(rs.getString("birthDate")),
            Gender.valueOf(rs.getString("gender")),
            rs.getString("interests"),
            rs.getString("city")
        );
    }

}
