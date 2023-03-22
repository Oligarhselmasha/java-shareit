package ru.practicum.shareit.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {
    private int id;
    @NotBlank
    @NotNull
    private String name;
    @NotBlank
    @NotNull
    @Email
    private String email;
}
