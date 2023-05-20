package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer userId) {
        return userService.getUser(userId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public User updateUser(@RequestBody UserDto userDto, @PathVariable("id") Integer userId) {
        return userService.updateUser(userDto, userId);
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable("id") Integer userId) {
        userService.removeUser(userId);
    }
}
