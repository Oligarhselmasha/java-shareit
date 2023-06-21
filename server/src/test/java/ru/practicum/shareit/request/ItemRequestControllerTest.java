package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.errorsHandling.ErrorHandler;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestFrontDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.constants.Variables.USER_HEADER;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private RequestService requestService;

    @InjectMocks
    private ItemRequestController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private ItemRequestDto itemRequestDto;

    private ItemRequestFrontDto itemRequestFrontDto;

    private final List<ItemDto> itemDtos = new ArrayList<>();

    private final List<ItemRequestFrontDto> itemRequestFrontDtos = new ArrayList<>();


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ErrorHandler.class)
                .build();

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Описание запроса");

        User user = new User();
        user.setId(1);
        user.setName("Кирилл");
        user.setEmail("kirill-bulanov@yandex.ru");

        ItemDto itemDto = new ItemDto();
        itemDto.setRequestId(1);
        itemDto.setName("Вещь");
        itemDto.setUser(user);
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);

        itemDtos.add(itemDto);

        itemRequestFrontDto = new ItemRequestFrontDto();
        itemRequestFrontDto.setCreated(LocalDateTime.now());
        itemRequestFrontDto.setItems(itemDtos);
        itemRequestFrontDto.setDescription("Описание запроса");
        itemRequestFrontDto.setId(1);

        itemRequestFrontDtos.add(itemRequestFrontDto);
    }

    @Test
    void createRequest() throws Exception {
        when(requestService.createRequest(any(), anyInt()))
                .thenReturn(itemRequestFrontDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestFrontDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(itemRequestFrontDto.getDescription())));
    }

    @Test
    void getOwnersAllRequests() throws Exception {
        when(requestService.getOwnersAllRequests(anyInt()))
                .thenReturn(itemRequestFrontDtos);

        mvc.perform(get("/requests")
                        .header(USER_HEADER, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(itemRequestFrontDtos.get(0).getDescription())))
                .andExpect(jsonPath("$[0].id", is(itemRequestFrontDtos.get(0).getId())));
    }

    @Test
    void getAllRequests() throws Exception {
        when(requestService.getAllRequests(any(), any(), anyInt()))
                .thenReturn(itemRequestFrontDtos);

        mvc.perform(get("/requests/all")
                        .header(USER_HEADER, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(itemRequestFrontDtos.get(0).getDescription())))
                .andExpect(jsonPath("$[0].id", is(itemRequestFrontDtos.get(0).getId())));
    }

    @Test
    void getRequest() throws Exception {
        when(requestService.getRequest(anyInt(), anyInt()))
                .thenReturn(itemRequestFrontDto);

        mvc.perform(get("/requests/1")
                        .header(USER_HEADER, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestFrontDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(itemRequestFrontDto.getDescription())));
    }
}