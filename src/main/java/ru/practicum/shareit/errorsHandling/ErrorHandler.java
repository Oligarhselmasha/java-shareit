package ru.practicum.shareit.errorsHandling;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.MissingException;
import ru.practicum.shareit.exceptions.ValidationException;


@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIValidationException(final ValidationException validationException) {
        return new ErrorResponse(
                validationException.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse missingException(final MissingException missingException) {
        return new ErrorResponse(
                missingException.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse conflictException(final ConflictException conflictException) {
        return new ErrorResponse(
                conflictException.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleException(final ConstraintViolationException constraintViolationException) {
        return new ErrorResponse(constraintViolationException.getMessage());
    }
}
