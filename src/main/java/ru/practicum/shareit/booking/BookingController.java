package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.constants.Variables.USER_HEADER;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader(USER_HEADER) Integer userId,
                                 @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateItem(@RequestHeader(USER_HEADER) Integer userId, @PathVariable("bookingId") Integer bookingId,
                              @RequestParam() Boolean approved) {
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(USER_HEADER) Integer userId, @PathVariable("bookingId") Integer bookingId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping()
    public List<BookingDto> getBookings(@RequestHeader(USER_HEADER) Integer userId, @RequestParam(defaultValue = "ALL",
            required = false) String state,
                                     @RequestParam(defaultValue = "0", required = false) Integer from,
                                     @RequestParam(defaultValue = "1", required = false) Integer size) {
        return bookingService.getBookingsPagged(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnersBookings(@RequestHeader(USER_HEADER) Integer userId,
                                           @RequestParam(defaultValue = "ALL", required = false) String state,
                                           @RequestParam(defaultValue = "0", required = false) Integer from,
                                           @RequestParam(defaultValue = "1", required = false) Integer size) {
        return bookingService.getOwnersBookingsPagged(userId, state, from, size);
    }
}
