package ru.practicum.shareit.booking;

import java.util.Optional;

public enum BookingStatus {
    WAITING, REJECTED, APPROVED, ALL, CURRENT, FUTURE;

    public static Optional<BookingStatus> from(String stringState) {
        for (BookingStatus state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
