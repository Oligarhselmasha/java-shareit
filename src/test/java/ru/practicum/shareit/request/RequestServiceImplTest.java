package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestFrontDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@Rollback
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestServiceImplTest {

    private final RequestService service;
    private final UserService userService;
    private final RequestService requestService;

    private ItemRequestDto makeRequestDto() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Описание запроса");
        return itemRequestDto;
    }

    @Test
    void createRequest() {
        ItemRequestDto itemRequestDto = makeRequestDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemRequestFrontDto itemRequestFrontDto = service.createRequest(itemRequestDto, user.getId());
        assertThat(itemRequestFrontDto.getId(), notNullValue());
    }

    @Test
    void getOwnersAllRequests() {
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemRequestDto itemRequestDto = new ItemRequestDto("Запрос");
        service.createRequest(itemRequestDto, user.getId());
        List<ItemRequestFrontDto> itemRequestFrontDtoList = service.getOwnersAllRequests(user.getId());
        assertThat(itemRequestFrontDtoList.get(0), notNullValue());
    }

    @Test
    void getAllRequests() {
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemRequestDto itemRequestDto = new ItemRequestDto("Запрос");
        service.createRequest(itemRequestDto, user.getId());
        User userAnother = userService.createUser(new UserDto("Кирил", "kirill-bulanov@narod.ru"));
        List<ItemRequestFrontDto> itemRequestFrontDtoList = service.getAllRequests("0", "1", userAnother.getId());
        assertThat(itemRequestFrontDtoList.get(0), notNullValue());
    }

    @Test
    void getRequest() {
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemRequestDto itemRequestDto = new ItemRequestDto("Запрос");
        ItemRequestFrontDto itemRequestFrontDto = service.createRequest(itemRequestDto, user.getId());
        ItemRequestFrontDto itemRequestFrontDto1 = service.getRequest(user.getId(), itemRequestFrontDto.getId());
        assertThat(itemRequestFrontDto1, is(itemRequestFrontDto));

    }
}