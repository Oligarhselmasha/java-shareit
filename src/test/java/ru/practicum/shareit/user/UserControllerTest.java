package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.errorsHandling.ErrorHandler;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private UserDto userDto;
    private User user;
    private User userUpdated;
    private List<User> users;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ErrorHandler.class)
                .build();
        user = new User();
        user.setEmail("kirill-bulanov@yandex.ru");
        user.setName("Кирилл");
        user.setId(1);

        userUpdated = new User();
        userUpdated.setEmail("kirill-bulanov@mail.ru");
        userUpdated.setName("Кирилл");
        userUpdated.setId(1);

        userDto = new UserDto();
        userDto.setEmail("kirill-bulanov@yandex.ru");
        userDto.setName("Кирилл");

        users = new ArrayList<>();
        users.add(user);
    }

    @Test
    void createUser() throws Exception {
        when(userService.createUser(any()))
                .thenReturn(user);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Integer.class))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.name", is(user.getName())));
    }
    @Test
    void createBadUser() throws Exception {
        when(userService.createUser(any()))
                .thenThrow(ValidationException.class);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError());
    }
    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(users);
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(users.get(0).getName())))
                .andExpect(jsonPath("$[0].id", is(users.get(0).getId()), Integer.class))
                .andExpect(jsonPath("$[0].email", is(users.get(0).getEmail())));
    }

    @Test
    void getUser() throws Exception {
        when(userService.getUser(anyInt()))
                .thenReturn(user);
        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Integer.class))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.name", is(user.getName())));
    }



    @Test
    void updateUser() throws Exception {
        userDto.setEmail("kirill-bulanov@yandex.ru");
        when(userService.updateUser(any(), anyInt()))
                .thenReturn(userUpdated);
        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userUpdated.getId()), Integer.class))
                .andExpect(jsonPath("$.email", is(userUpdated.getEmail())))
                .andExpect(jsonPath("$.name", is(userUpdated.getName())));
    }

    @Test
    void removeUser() throws Exception {
        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful());
    }
}
