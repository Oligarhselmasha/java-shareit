package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;


@Data
public class Request {
    private int id;
    private String requestThing;
    private User requester;
    private LocalDateTime created;
}
