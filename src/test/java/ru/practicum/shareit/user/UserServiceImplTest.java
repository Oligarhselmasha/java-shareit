package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@Rollback
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    private final UserService service;

    private UserDto makeUserDto() {
        UserDto dto = new UserDto();
        dto.setEmail("kirill-bulanov@yandex.ru");
        dto.setName("Кирилл");
        return dto;
    }

    @Test
    void createUser() {
        UserDto userDto = makeUserDto();
        User user = service.createUser(userDto);
        assertThat(user.getId(), notNullValue());
    }

    @Test
    void getAllUsers() {
        UserDto userDto = makeUserDto();
        User user = service.createUser(userDto);
        List<User> users =  service.getAllUsers();
        assertThat(users, hasItem(user));
        assertThat(users.get(0), allOf(hasProperty("email", equalTo("kirill-bulanov@yandex.ru"))));
        assertThat(users.get(0), allOf(hasProperty("id", equalTo(user.getId()))));
        assertThat(users.get(0), allOf(hasProperty("name", equalTo("Кирилл"))));
    }

    @Test
    void getUser() {
        UserDto userDto = makeUserDto();
        service.createUser(userDto);
        List<User> usersAll =  service.getAllUsers();
        User user = service.getUser(usersAll.get(0).getId());
        assertThat(user, allOf(hasProperty("email", equalTo("kirill-bulanov@yandex.ru"))));
        assertThat(user, allOf(hasProperty("name", equalTo("Кирилл"))));
        assertThat(user, allOf(hasProperty("id", equalTo(user.getId()))));
    }

    @Test
    void updateUser() {
        UserDto userDto = makeUserDto();
        userDto.setEmail("kirill-bulanov@mail.ru");
        User user = service.updateUser(userDto, 1);
        assertThat(user, allOf(hasProperty("email", equalTo("kirill-bulanov@mail.ru"))));
    }

    @Test
    void removeUser() {
        UserDto userDto = makeUserDto();
        service.createUser(userDto);
        List<User> usersAll =  service.getAllUsers();
        service.removeUser(usersAll.get(0).getId());
        List<User> users = service.getAllUsers();
        assertThat(users, empty());
    }
}
