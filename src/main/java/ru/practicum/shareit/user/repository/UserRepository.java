package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {


    List<User> getAllUsers();

    User getUser(Integer userId);

    User createUser(User user);

    User updateUser(User user, Integer userId);

    void removeUser(Integer userId);
}
