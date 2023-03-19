package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class Request {
    private int id;
    private String requestThing;
    private User requester;
    private LocalDateTime created;
}
