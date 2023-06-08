package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.MissingException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@Rollback
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    private final BookingService service;
    private final UserService userService;
    private final ItemService itemService;

    private BookingDto makeBookingDto() {
        BookingDto dto = new BookingDto();
        dto.setStartTime(LocalDateTime.now().plusMinutes(1));
        dto.setEndTime(LocalDateTime.now().plusMinutes(2));
        return dto;
    }

    @Test
    void createBooking() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        assertThat(booking.getBookingId(), notNullValue());
        assertThat(booking.getUser().getId(), is(anotherUser.getId()));
        assertThat(booking.getItem().getName(), is(itemDto.getName()));
    }

    @Test
    void createBookingItemIsNotExist() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId() * (-1));
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.createBooking(bookingDto, anotherUser.getId()));
    }

    @Test
    void createBookingUserIsNotExist() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.createBooking(bookingDto, anotherUser.getId() * (-1)));
    }

    @Test
    void createBookingUserIsBookersItem() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.createBooking(bookingDto, user.getId()));
    }

    @Test
    void createBookingWrongDate() {
        BookingDto bookingDto = makeBookingDto();
        bookingDto.setStartTime(LocalDateTime.now().minusHours(1));
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> service.createBooking(bookingDto, anotherUser.getId()));
    }

    @Test
    void createBookingNotAvailable() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(false);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> service.createBooking(bookingDto, anotherUser.getId()));
    }

    @Test
    void createNullDtoBooking() {
        BookingDto bookingDto = null;
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.createBooking(bookingDto, anotherUser.getId()));
    }

    @Test
    void updateBooking() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        Booking bookingForCheck = service.updateBooking(user.getId(), booking.getBookingId(), false);
        assertThat(bookingForCheck.getBookingId(), notNullValue());
        assertThat(bookingForCheck.getUser().getId(), is(anotherUser.getId()));
        assertThat(bookingForCheck.getItem().getName(), is(itemDto.getName()));
        assertThat(bookingForCheck.getStatus(), is(BookingStatus.REJECTED));
    }

    @Test
    void updateBookingIsNotUserThing() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.updateBooking(user.getId() * (-1), booking.getBookingId(), false));
    }

    @Test
    void updateBookingIsWrong() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.updateBooking(user.getId(), booking.getBookingId() * (-1), false));
    }

    @Test
    void updateBookingWrongStatusFalse() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        service.updateBooking(user.getId(), booking.getBookingId(), false);
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> service.updateBooking(user.getId(), booking.getBookingId(), false));
    }

    @Test
    void updateBookingWrongStatusTrue() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        service.updateBooking(user.getId(), booking.getBookingId(), true);
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> service.updateBooking(user.getId(), booking.getBookingId(), true));
    }

    @Test
    void getBookingWrong() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.getBooking(booking.getBookingId() * (-1), user.getId()));
    }

    @Test
    void getBooking() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        Booking bookingForCheck = service.getBooking(booking.getBookingId(), user.getId());
        assertThat(bookingForCheck.getBookingId(), notNullValue());
        assertThat(bookingForCheck.getUser().getId(), is(anotherUser.getId()));
        assertThat(bookingForCheck.getItem().getName(), is(itemDto.getName()));
    }

    @Test
    void getBookingWrongUserId() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.getBooking(booking.getBookingId(), user.getId() * (-1)));
    }

    @Test
    void getBookings() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        service.updateBooking(user.getId(), booking.getBookingId(), false);
        List<Booking> bookingsForCheck = service.getBookings(anotherUser.getId(), "REJECTED");
        assertThat(bookingsForCheck.get(0).getBookingId(), notNullValue());
        assertThat(bookingsForCheck.get(0).getUser().getId(), is(anotherUser.getId()));
        assertThat(bookingsForCheck.get(0).getItem().getName(), is(itemDto.getName()));
        assertThat(bookingsForCheck.get(0).getStatus(), is(BookingStatus.REJECTED));
    }

    @Test
    void getBookingsWrongUser() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        service.updateBooking(user.getId(), booking.getBookingId(), false);
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.getBookings(anotherUser.getId()*(-1), "REJECTED"));
    }

    @Test
    void getBookingsWrongStatus() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        service.updateBooking(user.getId(), booking.getBookingId(), false);
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> service.getBookings(anotherUser.getId(), "DGFSBVGDSD"));
    }

    @Test
    void getOwnersBookings() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        service.updateBooking(user.getId(), booking.getBookingId(), false);
        List<Booking> bookingsForCheck = service.getOwnersBookings(user.getId(), "REJECTED");
        assertThat(bookingsForCheck.get(0).getBookingId(), notNullValue());
        assertThat(bookingsForCheck.get(0).getUser().getId(), is(anotherUser.getId()));
        assertThat(bookingsForCheck.get(0).getItem().getName(), is(itemDto.getName()));
        assertThat(bookingsForCheck.get(0).getStatus(), is(BookingStatus.REJECTED));
    }

    @Test
    void getOwnersBookingsWrongUser() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        service.updateBooking(user.getId(), booking.getBookingId(), false);
        assertThatExceptionOfType(MissingException.class)
                .isThrownBy(() -> service.getOwnersBookings(user.getId()*(-1), "REJECTED"));
    }

    @Test
    void getOwnersBookingsWrongStatus() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        service.updateBooking(user.getId(), booking.getBookingId(), false);
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> service.getOwnersBookings(user.getId(), "DVGSDVFSDVS"));
    }

    @Test
    void getBookingsPagged() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        service.updateBooking(user.getId(), booking.getBookingId(), false);
        List<Booking> bookingsForCheck = service.getBookingsPagged(anotherUser.getId(), "REJECTED", "0", "1");
        assertThat(bookingsForCheck.get(0).getBookingId(), notNullValue());
        assertThat(bookingsForCheck.get(0).getUser().getId(), is(anotherUser.getId()));
        assertThat(bookingsForCheck.get(0).getItem().getName(), is(itemDto.getName()));
        assertThat(bookingsForCheck.get(0).getStatus(), is(BookingStatus.REJECTED));
    }

    @Test
    void getBookingsPaggedWithoutParams() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        service.updateBooking(user.getId(), booking.getBookingId(), false);
        List<Booking> bookingsForCheck = service.getBookingsPagged(anotherUser.getId(), "REJECTED", "null", "null");
        assertThat(bookingsForCheck.get(0).getBookingId(), notNullValue());
        assertThat(bookingsForCheck.get(0).getUser().getId(), is(anotherUser.getId()));
        assertThat(bookingsForCheck.get(0).getItem().getName(), is(itemDto.getName()));
        assertThat(bookingsForCheck.get(0).getStatus(), is(BookingStatus.REJECTED));
    }

    @Test
    void getBookingsPaggedWrongParams() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        service.updateBooking(user.getId(), booking.getBookingId(), false);
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> service.getBookingsPagged(anotherUser.getId(), "REJECTED", "-1", "-1"));
    }

    @Test
    void getOwnersBookingsPagged() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        service.updateBooking(user.getId(), booking.getBookingId(), false);
        List<Booking> bookingsForCheck = service.getOwnersBookingsPagged(user.getId(), "REJECTED", "0", "1");
        assertThat(bookingsForCheck.get(0).getBookingId(), notNullValue());
        assertThat(bookingsForCheck.get(0).getUser().getId(), is(anotherUser.getId()));
        assertThat(bookingsForCheck.get(0).getItem().getName(), is(itemDto.getName()));
        assertThat(bookingsForCheck.get(0).getStatus(), is(BookingStatus.REJECTED));
    }

    @Test
    void getOwnersBookingsPaggedBigSize() {
        BookingDto bookingDto = makeBookingDto();
        User user = userService.createUser(new UserDto("Кирилл", "kirill-bulanov@yandex.ru"));
        User anotherUser = userService.createUser(new UserDto("Не Кирилл", "kirill-bulanov@mail.ru"));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setIsFree(true);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        bookingDto.setItemId(item.getId());
        Booking booking = service.createBooking(bookingDto, anotherUser.getId());
        service.updateBooking(user.getId(), booking.getBookingId(), false);
        List<Booking> bookingsForCheck = service.getOwnersBookingsPagged(user.getId(), "REJECTED", "0", "777");
        assertThat(bookingsForCheck.get(0).getBookingId(), notNullValue());
        assertThat(bookingsForCheck.get(0).getUser().getId(), is(anotherUser.getId()));
        assertThat(bookingsForCheck.get(0).getItem().getName(), is(itemDto.getName()));
        assertThat(bookingsForCheck.get(0).getStatus(), is(BookingStatus.REJECTED));
    }
}