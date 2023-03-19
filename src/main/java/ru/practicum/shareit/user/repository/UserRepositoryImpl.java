package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.MissingException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private int userId = 0;
    private final List<User> users = new ArrayList<>();

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public User getUser(Integer userId) {
        return users.stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new MissingException("No found with id: " + userId));
    }

    @Override
    public User createUser(User user) {
        validationUsersEmail(user);
        userId++;
        user.setId(userId);
        users.add(user);
        return user;
    }

    @Override
    public User updateUser(User user, Integer userId) {
        User userUpdated = getUser(userId);
        if (user.getEmail() != null) {
            validationUsersEmail(user, userId);
            userUpdated.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userUpdated.setName(user.getName());
        }
        removeUser(userId);
        users.add(userUpdated);
        return userUpdated;
    }

    @Override
    public void removeUser(Integer userId) {
        User userForRemove = users.stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new MissingException("No found with id: " + userId));
        users.remove(userForRemove);
    }

    private void validationUsersEmail(User user, int userId) {
        if (users.stream()
                .anyMatch(u -> u.getEmail()
                        .equals(user.getEmail()) && u.getId() != userId)) {
            throw new ConflictException("Email already exist");
        }
    }

    private void validationUsersEmail(User user) {
        if (users.stream()
                .anyMatch(u -> u.getEmail()
                        .equals(user.getEmail()))) {
            throw new ConflictException("Email already exist");
        }
    }
}
