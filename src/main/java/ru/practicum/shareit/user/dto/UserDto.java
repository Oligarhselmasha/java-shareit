package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Builder
@AllArgsConstructor
public class UserDto {
    @NotBlank
    @NotNull
    private String name;
    @NotBlank
    @NotNull
    @Email
    private String email;
}
