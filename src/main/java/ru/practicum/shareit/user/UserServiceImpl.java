package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.MissingException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UsersMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UsersMapper userMapper;

    @Transactional
    @Override
    public User createUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return repository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUser(Integer userId) {
        return repository.findById(userId).orElseThrow(() -> {
            throw new MissingException("is not exist");
        });
    }

    @Override
    public User updateUser(UserDto userDto, Integer userId) {
        User user = userMapper.toUser(userDto);
        if (user.getEmail() != null) {
            if (repository.existsByEmail(user.getEmail())) {
                validationUsersEmail(user, userId);
            }
        } else {
            user.setEmail(repository.findById(userId).orElseThrow().getEmail());
        }
        if (user.getName() == null) {
            user.setName(repository.findById(userId).orElseThrow().getName());
        }
        user.setId(userId);
        return repository.save(user);
    }

    @Override
    public void removeUser(Integer userId) {
        repository.deleteAllById(Collections.singleton(userId));
    }

    private void validationUsersEmail(User user, Integer userId) {
        User updateUser = repository.findByEmailIgnoreCaseOrderByIdAsc(user.getEmail()).orElseThrow();
        if (updateUser.getId() != userId) {
            throw new ConflictException("Email already exist");
        }
    }
}
