package ru.practicum.shareit.booking;

import lombok.Data;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private int id;

    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private String status;
    private boolean isAccepted;
    private String review;
}
