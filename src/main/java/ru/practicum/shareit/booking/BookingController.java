package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.constants.Variables.USER_HEADER;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestHeader(USER_HEADER) Integer userId,
                                 @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking updateItem(@RequestHeader(USER_HEADER) Integer userId, @PathVariable("bookingId") Integer bookingId,
                              @RequestParam() Boolean approved) {
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(@RequestHeader(USER_HEADER) Integer userId, @PathVariable("bookingId") Integer bookingId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping()
    public List<Booking> getBookings(@RequestHeader(USER_HEADER) Integer userId, @RequestParam(defaultValue = "ALL",
            required = false) String state) {
        return bookingService.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getOwnersBookings(@RequestHeader(USER_HEADER) Integer userId,
                                           @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingService.getOwnersBookings(userId, state);
    }
}
