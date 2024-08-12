package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must contain the @ symbol")
    private String email;

    @NotBlank(message = "Login cannot be empty and must not contain spaces")
    @Size(min = 3, message = "Login cannot be empty")
    @Pattern(regexp = "\\S+", message = "Login must not contain spaces")
    private String login;

    private String name;

    @NotNull(message = "Date of birth cannot be empty")
    @Past(message = "Date of birth cannot be in the future")
    @JsonFormat(pattern = ("yyyy-MM-dd"))
    private LocalDate birthday;

    public void setName(String name) {
        if (name == null || name.isEmpty()) this.name = login;
        else this.name = name;
    }

    Set<Integer> friends;
}
