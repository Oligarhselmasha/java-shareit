package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUser(Integer userId);

    User createUser(UserDto user);

    User updateUser(UserDto user, Integer userId);

    void removeUser(Integer userId);
}
