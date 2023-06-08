package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.MissingException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@Rollback
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {

    private final ItemService service;
    private final UserService userService;

    private final BookingService bookingService;


    private ItemDto makeItemDto() {
        ItemDto dto = new ItemDto();
        dto.setDescription("Описание вещи");
        dto.setName("Вещь");
        dto.setIsFree(true);
        return dto;
    }

    @Test
    void createItem() {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemDto dtoOut = service.createItem(dtoIn, user.getId());
        assertThat(dtoOut.getId(), notNullValue());
    }

    @Test
    void createItemUserIsNotExist() {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.createItem(dtoIn, user.getId() * (-1)));
    }

    @Test
    void updateItemIsNotExist() {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemDto dtoOut = service.createItem(dtoIn, user.getId());
        dtoOut.setDescription("Другая вещь");
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.updateItem(dtoOut, user.getId(), dtoOut.getId() * (-1)));
    }

    @Test
    void updateItemUserIsNotExist() {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemDto dtoOut = service.createItem(dtoIn, user.getId());
        dtoOut.setDescription("Другая вещь");
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.updateItem(dtoOut, user.getId() * (-1), dtoOut.getId()));
    }

    @Test
    void updateItemWrongUser() {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User userAnother = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto dtoOut = service.createItem(dtoIn, userAnother.getId());
        dtoOut.setDescription("Другая вещь");
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.updateItem(dtoOut, user.getId(), dtoOut.getId()));
    }

    @Test
    void updateItem() {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemDto dtoOut = service.createItem(dtoIn, user.getId());
        dtoOut.setDescription("Другая вещь");
        ItemDto dtoOutForCheck = service.updateItem(dtoOut, user.getId(), dtoOut.getId());
        assertThat(dtoOutForCheck, allOf(hasProperty("description", equalTo("Другая вещь"))));
    }

    @Test
    void updateItemNullsParameters() {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemDto dtoOut = service.createItem(dtoIn, user.getId());
        int itemId = dtoOut.getId();
        dtoOut.setDescription(null);
        dtoOut.setId(null);
        dtoOut.setName(null);
        dtoOut.setIsFree(null);
        ItemDto dtoOutForCheck = service.updateItem(dtoOut, user.getId(), itemId);
        assertThat(dtoOutForCheck, allOf(hasProperty("description", equalTo("Описание вещи"))));
    }

    @Test
    void getItem() {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemDto dtoOutCreated = service.createItem(dtoIn, user.getId());

        User anotherUser = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@mail.ru"));

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(dtoOutCreated.getId());
        bookingDto.setBookerId(user.getId());
        bookingDto.setStartTime(LocalDateTime.now().plusSeconds(1));
        bookingDto.setEndTime(LocalDateTime.now().plusSeconds(2));
        Booking booking = bookingService.createBooking(bookingDto, anotherUser.getId());
        bookingService.updateBooking(user.getId(), booking.getBookingId(), true);

        ItemDto dtoOut = service.getItem(dtoOutCreated.getId(), user.getId());
        assertThat(dtoOut, allOf(hasProperty("description", equalTo("Описание вещи"))));
    }

    @Test
    void getItemWithLastBookings() throws InterruptedException {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemDto dtoOutCreated = service.createItem(dtoIn, user.getId());

        User anotherUser = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@mail.ru"));

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(dtoOutCreated.getId());
        bookingDto.setBookerId(user.getId());
        bookingDto.setStartTime(LocalDateTime.now().plusSeconds(1));
        bookingDto.setEndTime(LocalDateTime.now().plusSeconds(2));
        Booking booking = bookingService.createBooking(bookingDto, anotherUser.getId());
        bookingService.updateBooking(user.getId(), booking.getBookingId(), true);

        Thread.sleep(3000);
        ItemDto dtoOut = service.getItem(dtoOutCreated.getId(), user.getId());
        assertThat(dtoOut, allOf(hasProperty("description", equalTo("Описание вещи"))));
    }

    @Test
    void getItemIsNotExist() {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemDto dtoOutCreated = service.createItem(dtoIn, user.getId());

        User anotherUser = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@mail.ru"));

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(dtoOutCreated.getId());
        bookingDto.setBookerId(user.getId());
        bookingDto.setStartTime(LocalDateTime.now().plusSeconds(1));
        bookingDto.setEndTime(LocalDateTime.now().plusSeconds(2));
        Booking booking = bookingService.createBooking(bookingDto, anotherUser.getId());
        bookingService.updateBooking(user.getId(), booking.getBookingId(), true);

        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.getItem(dtoOutCreated.getId() * (-1), user.getId()));
    }

    @Test
    void getUsersItems() {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        dtoIn.setUser(user);
        service.createItem(dtoIn, user.getId());
        List<ItemDto> dtoOuts = service.getUsersItems(user.getId());
        assertThat(dtoOuts.get(0), allOf(hasProperty("description", equalTo("Описание вещи"))));
    }

    @Test
    void getItemByQuery() {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        dtoIn.setUser(user);
        service.createItem(dtoIn, user.getId());
        service.getItemByQuery((long) user.getId(), "Описание");
        List<ItemDto> dtoOuts = service.getUsersItems(user.getId());
        assertThat(dtoOuts.get(0), allOf(hasProperty("description", equalTo("Описание вещи"))));
    }

    @Test
    void getItemByEmptyQuery() {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        dtoIn.setUser(user);
        service.createItem(dtoIn, user.getId());
        service.getItemByQuery((long) user.getId(), "");
        List<ItemDto> dtoOuts = service.getUsersItems(user.getId());
        assertThat(dtoOuts.get(0), allOf(hasProperty("description", equalTo("Описание вещи"))));
    }

    @Test
    void createItemsComment() throws InterruptedException {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@mail.ru"));
        dtoIn.setUser(user);
        ItemDto itemDtoOut = service.createItem(dtoIn, user.getId());

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemDtoOut.getId());
        bookingDto.setBookerId(user.getId());
        bookingDto.setStartTime(LocalDateTime.now().plusSeconds(1));
        bookingDto.setEndTime(LocalDateTime.now().plusSeconds(2));
        Booking booking = bookingService.createBooking(bookingDto, anotherUser.getId());
        bookingService.updateBooking(user.getId(), booking.getBookingId(), true);


        CommentDto commentDto = new CommentDto();
        commentDto.setAuthorName(anotherUser.getName());
        commentDto.setText("Комментарий");

        Thread.sleep(2000);
        CommentDto commentDtoOut = service.createItemsComment(commentDto, anotherUser.getId(), itemDtoOut.getId());

        assertThat(commentDtoOut.getId(), notNullValue());
    }

    @Test
    void createItemsEmptyComment() throws InterruptedException {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@mail.ru"));
        dtoIn.setUser(user);
        ItemDto itemDtoOut = service.createItem(dtoIn, user.getId());

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemDtoOut.getId());
        bookingDto.setBookerId(user.getId());
        bookingDto.setStartTime(LocalDateTime.now().plusSeconds(1));
        bookingDto.setEndTime(LocalDateTime.now().plusSeconds(2));
        Booking booking = bookingService.createBooking(bookingDto, anotherUser.getId());
        bookingService.updateBooking(user.getId(), booking.getBookingId(), true);


        CommentDto commentDto = new CommentDto();
        commentDto.setAuthorName(anotherUser.getName());
        commentDto.setText("");

        Thread.sleep(2000);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> service.createItemsComment(commentDto, anotherUser.getId(), itemDtoOut.getId()));
    }

    @Test
    void createItemsBookingIsNotExist() throws InterruptedException {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@mail.ru"));
        dtoIn.setUser(user);
        ItemDto itemDtoOut = service.createItem(dtoIn, user.getId());

        CommentDto commentDto = new CommentDto();
        commentDto.setAuthorName(anotherUser.getName());
        commentDto.setText("");

        Thread.sleep(2000);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> service.createItemsComment(commentDto, anotherUser.getId(), itemDtoOut.getId()));
    }

    @Test
    void removeItem() {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        dtoIn.setUser(user);
        ItemDto itemDtoOut = service.createItem(dtoIn, user.getId());
        service.removeItem(user.getId(), itemDtoOut.getId());
        List<ItemDto> itemDtos = service.getUsersItems(user.getId());
        assertThat(itemDtos, is(empty()));
    }

    @Test
    void removeWrongItem() {
        ItemDto dtoIn = makeItemDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        dtoIn.setUser(user);
        ItemDto itemDtoOut = service.createItem(dtoIn, user.getId());
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> service.removeItem(user.getId() * (-1), itemDtoOut.getId()));
    }
}