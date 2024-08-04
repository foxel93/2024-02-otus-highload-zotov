package ru.otus.hw.dto;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDto {
    @NotNull
    @NotEmpty
    @JsonView(View.Public.class)
    private String firstName;

    @NotNull
    @NotEmpty
    @JsonView(View.Public.class)
    private String secondName;
}
