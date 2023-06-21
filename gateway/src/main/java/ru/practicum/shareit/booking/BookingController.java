package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.ValidationException;

import javax.validation.Valid;

import static ru.practicum.shareit.constants.Variables.USER_HEADER;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;

    @GetMapping()
    public ResponseEntity<Object> getBookings(@RequestHeader(USER_HEADER) Integer userId, @RequestParam(defaultValue = "ALL",
            required = false) String state,
                                              @RequestParam(defaultValue = "0", required = false) Integer from,
                                              @RequestParam(defaultValue = "1", required = false) Integer size) {
        BookingStatus status = BookingStatus.from(state)
                .orElseThrow(() -> new ValidationException("Unknown state: UNSUPPORTED_STATUS"));
        return bookingClient.getBookingsPagged(userId, status, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(USER_HEADER) Integer userId,
                                                @Valid @RequestBody BookingDto bookingDto) {
        return bookingClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_HEADER) Integer userId, @PathVariable("bookingId") Integer bookingId,
                                             @RequestParam() Boolean approved) {
        return bookingClient.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(USER_HEADER) Integer userId, @PathVariable("bookingId") Integer bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnersBookings(@RequestHeader(USER_HEADER) Integer userId,
                                                    @RequestParam(defaultValue = "ALL", required = false) String state,
                                                    @RequestParam(defaultValue = "0", required = false) Integer from,
                                                    @RequestParam(defaultValue = "1", required = false) Integer size) {
        BookingStatus status = BookingStatus.from(state)
                .orElseThrow(() -> new ValidationException("Unknown state: UNSUPPORTED_STATUS"));
        return bookingClient.getOwnersBookingsPagged(userId, status, from, size);
    }
}
