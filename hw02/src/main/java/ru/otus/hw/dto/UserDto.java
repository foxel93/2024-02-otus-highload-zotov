package ru.otus.hw.dto;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.otus.hw.models.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotNull
    @NotEmpty
    @ToString.Exclude
    @JsonView(View.Internal.class)
    private String username;

    @NotNull
    @NotEmpty
    @ToString.Exclude
    @JsonView(View.Internal.class)
    private String password;

    @NotNull
    @NotEmpty
    @JsonView(View.Public.class)
    private String firstName;

    @NotNull
    @NotEmpty
    @JsonView(View.Public.class)
    private String secondName;

    @NotNull
    @JsonView(View.Public.class)
    private LocalDate birthDate;

    @NotNull
    @JsonView(View.Public.class)
    private Gender gender;

    @NotNull
    @NotEmpty
    @JsonView(View.Public.class)
    private String interests;

    @NotNull
    @NotEmpty
    @JsonView(View.Public.class)
    private String city;
}
