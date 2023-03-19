package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;

    @Override
    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }

    @Override
    public User getUser(Integer userId) {
        return repository.getUser(userId);
    }

    @Override
    public User createUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return repository.createUser(user);
    }

    @Override
    public User updateUser(UserDto userDto, Integer userId) {
        User user = userMapper.toUser(userDto);
        return repository.updateUser(user, userId);
    }

    @Override
    public void removeUser(Integer userId) {
        repository.removeUser(userId);
    }
}
