package ru.practicum.shareit.errorsHandling;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter

public class ErrorResponse {
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}
