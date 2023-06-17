package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.errorsHandling.ErrorHandler;
import ru.practicum.shareit.exceptions.MissingException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.constants.Variables.USER_HEADER;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService service;

    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private ItemDto itemDtoIn;

    private ItemDto itemDtoOut;

    private CommentDto commentDto;

    private final List<ItemDto> itemDtos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ErrorHandler.class)
                .build();

        User user = new User();
        user.setId(1);
        user.setName("Кирилл");
        user.setEmail("kirill-bulanov@yandex.ru");

        itemDtoIn = new ItemDto();
        itemDtoIn.setName("Вещь");
        itemDtoIn.setDescription("Описание вещи");
        itemDtoIn.setIsFree(true);

        itemDtoOut = new ItemDto();
        itemDtoOut.setRequestId(1);
        itemDtoOut.setName("Вещь");
        itemDtoOut.setUser(user);
        itemDtoOut.setDescription("Описание вещи");
        itemDtoOut.setIsFree(true);

        commentDto = new CommentDto();
        commentDto.setAuthorName(user.getName());
        commentDto.setCreated(LocalDateTime.now());
        commentDto.setText("Комментарий");
        commentDto.setId(1);

        itemDtos.add(itemDtoOut);
    }

    @Test
    void createItem() throws Exception {
        when(service.createItem(any(), anyInt()))
                .thenReturn(itemDtoOut);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOut.getId()), Integer.class))
                .andExpect(jsonPath("$.available", is(itemDtoOut.getIsFree())))
                .andExpect(jsonPath("$.description", is(itemDtoOut.getDescription())));
    }

    @Test
    void createItemUserIsNotExist() throws Exception {
        when(service.createItem(any(), anyInt()))
                .thenThrow(MissingException.class);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createItemsComment() throws Exception {
        when(service.createItemsComment(any(), anyInt(), anyInt()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1), Integer.class))
                .andExpect(jsonPath("$.text", is("Комментарий")))
                .andExpect(jsonPath("$.authorName", is("Кирилл")));
    }

    @Test
    void updateItem() throws Exception {
        when(service.updateItem(any(), anyInt(), anyInt()))
                .thenReturn(itemDtoOut);

        mvc.perform(patch("/items/1/")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOut.getId()), Integer.class))
                .andExpect(jsonPath("$.available", is(itemDtoOut.getIsFree())))
                .andExpect(jsonPath("$.description", is(itemDtoOut.getDescription())));
    }

    @Test
    void getItem() throws Exception {
        when(service.getItem(anyInt(), anyInt()))
                .thenReturn(itemDtoOut);

        mvc.perform(get("/items/1/")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOut.getId()), Integer.class))
                .andExpect(jsonPath("$.available", is(itemDtoOut.getIsFree())))
                .andExpect(jsonPath("$.description", is(itemDtoOut.getDescription())));
    }

    @Test
    void removeItem() throws Exception {
        mvc.perform(delete("/items/1/")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void removeItemBad() throws Exception {
        mvc.perform(delete("/items/1/")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getUsersItems() throws Exception {
        when(service.getUsersItems(anyInt()))
                .thenReturn(itemDtos);

        mvc.perform(get("/items/")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(itemDtos.get(0).getName())))
                .andExpect(jsonPath("$[0].id", is(itemDtos.get(0).getId()), Integer.class))
                .andExpect(jsonPath("$[0].available", is(itemDtos.get(0).getIsFree())));

    }

    @Test
    void findItemByQuery() throws Exception {
        when(service.getItemByQuery(anyLong(), anyString()))
                .thenReturn(itemDtos);

        mvc.perform(get("/items/search/")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", "Описание")
                        .header(USER_HEADER, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(itemDtos.get(0).getName())))
                .andExpect(jsonPath("$[0].id", is(itemDtos.get(0).getId()), Integer.class))
                .andExpect(jsonPath("$[0].available", is(itemDtos.get(0).getIsFree())));
    }
}