package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.MissingException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private int userId = 0;
    private final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Integer userId) {
        return users.values().stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new MissingException("No found with id: " + userId));
    }

    @Override
    public User createUser(User user) {
        validationUsersEmail(user);
        userId++;
        user.setId(userId);
        users.put(userId, user);
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
        users.put(userId, userUpdated);
        return userUpdated;
    }

    @Override
    public void removeUser(Integer userId) {
        users.values().stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new MissingException("No found with id: " + userId));
        users.remove(userId);
    }

    private void validationUsersEmail(User user, int userId) {
        if (users.values().stream()
                .anyMatch(u -> u.getEmail()
                        .equals(user.getEmail()) && u.getId() != userId)) {
            throw new ConflictException("Email already exist");
        }
    }

    private void validationUsersEmail(User user) {
        if (users.values().stream()
                .anyMatch(u -> u.getEmail()
                        .equals(user.getEmail()))) {
            throw new ConflictException("Email already exist");
        }
    }
}
