package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class UserTest {
    @Autowired
    private JacksonTester<User> json;

    @Test
    void testUserDto() throws Exception {
        User user = new User(                1,
                "Kirill",
                "kirill-bulanov@yandex.ru"
                );

        JsonContent<User> result = json.write(user);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Kirill");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("kirill-bulanov@yandex.ru");
    }
}