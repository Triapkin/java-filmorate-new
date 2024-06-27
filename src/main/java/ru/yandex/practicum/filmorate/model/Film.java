package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;

    @NotBlank(message = "The title cannot be empty")
    private String name;

    @Size(max = 200, message = "Maximum description length: 200 characters")
    private String description;

    @NotNull(message = "Release date cannot be empty")
    @PastOrPresent(message = "Release date cannot be in the future")
    @JsonFormat(pattern = ("yyyy-MM-dd"))
    private LocalDate releaseDate;

    @Positive(message = "The length of the movie must be a positive number")
    private int duration;
}
