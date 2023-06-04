//package ru.practicum.shareit.user.mapper;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import ru.practicum.shareit.user.UserService;
//import ru.practicum.shareit.user.dto.UserDto;
//import ru.practicum.shareit.user.model.User;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@RequiredArgsConstructor
//class UsersMapperTest {
//
//    @Mock
//    private final UsersMapper usersMapper;
//
//    @Test
//    void toUser() {
//        UserDto userDto = new UserDto("Kirill", "kirill-bulanov@yandex.ru");
//        User user = usersMapper.toUser(userDto);
//        assertThat(user, allOf(hasProperty("email", equalTo("kirill-bulanov@yandex.ru"))));
//        assertThat(user, allOf(hasProperty("name", equalTo("Kirill"))));
//    }
//}