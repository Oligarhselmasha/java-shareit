package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.MissingException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestFrontDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.MatcherAssert.assertThat;
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
    private final ItemService itemService;

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
    void createRequestDtoIsNull() {
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.createRequest(null, user.getId()));
    }

    @Test
    void getRequest() {
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User userAnother = userService.createUser(new UserDto("Кирил", "kirill-bulanov@mail.ru"));
        ItemRequestDto itemRequestDto = new ItemRequestDto("Запрос");
        ItemRequestFrontDto itemRequestFrontDto = service.createRequest(itemRequestDto, user.getId());
        ItemDto itemDto = new ItemDto();
        itemDto.setRequestId(itemRequestFrontDto.getId());
        itemDto.setIsFree(true);
        itemDto.setName("Вещь");
        itemDto.setDescription("Вещь");
        itemDto.setUser(userAnother);
        itemService.createItem(itemDto, userAnother.getId());
        ItemRequestFrontDto itemRequestFrontDto1 = service.getRequest(user.getId(), itemRequestFrontDto.getId());
        assertThat(itemRequestFrontDto1.getId(), notNullValue());
    }

    @Test
    void createRequestWrongUser() {
        ItemRequestDto itemRequestDto = makeRequestDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.createRequest(itemRequestDto, user.getId() * (-1)));
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
    void getOwnersAllRequestsWrongUser() {
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemRequestDto itemRequestDto = new ItemRequestDto("Запрос");
        service.createRequest(itemRequestDto, user.getId());
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.getOwnersAllRequests(user.getId() * (-1)));
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
    void getAllRequestsNotFrom0() {
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemRequestDto itemRequestDto = new ItemRequestDto("Запрос");
        service.createRequest(itemRequestDto, user.getId());
        User userAnother = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemRequestDto itemRequestDtoAnother = new ItemRequestDto("Запрос2");
        service.createRequest(itemRequestDtoAnother, userAnother.getId());
        User userAnotherMore = userService.createUser(new UserDto("Кирил", "kirill-bulanov@narod.ru"));
        List<ItemRequestFrontDto> itemRequestFrontDtoList = service.getAllRequests("1", "1", userAnotherMore.getId());
        assertThat(itemRequestFrontDtoList.get(0), notNullValue());
    }

    @Test
    void getAllRequestsWithoutParams() {
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemRequestDto itemRequestDto = new ItemRequestDto("Запрос");
        service.createRequest(itemRequestDto, user.getId());
        User userAnother = userService.createUser(new UserDto("Кирил", "kirill-bulanov@narod.ru"));
        List<ItemRequestFrontDto> itemRequestFrontDtoList = service.getAllRequests("null", "null", userAnother.getId());
        assertThat(itemRequestFrontDtoList.get(0), notNullValue());
    }

    @Test
    void getAllRequestsWrongParams() {
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemRequestDto itemRequestDto = new ItemRequestDto("Запрос");
        service.createRequest(itemRequestDto, user.getId());
        User userAnother = userService.createUser(new UserDto("Кирил", "kirill-bulanov@narod.ru"));
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> service.getAllRequests("-1", "-1", userAnother.getId()));
    }

    @Test
    void getRequestUserIsNotExist() {
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemRequestDto itemRequestDto = new ItemRequestDto("Запрос");
        ItemRequestFrontDto itemRequestFrontDto = service.createRequest(itemRequestDto, user.getId());
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.getRequest(user.getId() * (-1), itemRequestFrontDto.getId()));
    }

    @Test
    void getRequestIsNotExist() {
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemRequestDto itemRequestDto = new ItemRequestDto("Запрос");
        ItemRequestFrontDto itemRequestFrontDto = service.createRequest(itemRequestDto, user.getId());
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.getRequest(user.getId(), itemRequestFrontDto.getId() * (-1)));
    }
}