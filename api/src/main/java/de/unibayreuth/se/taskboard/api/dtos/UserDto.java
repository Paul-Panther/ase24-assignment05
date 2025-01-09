package de.unibayreuth.se.taskboard.api.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.UUID;

//TODO: Add DTO for users.
public record UserDto(
        @Nullable
        UUID id,
        @NotNull
        LocalDateTime createdAt,
        @NotNull
        @NotBlank // Ensures the string is not null or empty
        @Size(max = 255, message = "Name can be at most 255 characters long.")
        String name
) { }
