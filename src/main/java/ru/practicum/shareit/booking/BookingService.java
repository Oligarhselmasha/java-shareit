package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto, Integer userId);

    BookingDto updateBooking(Integer userId, Integer bookingId, Boolean isApproved);

    BookingDto getBooking(Integer bookingId);

    BookingDto getBooking(Integer bookingId, Integer userId);

    List<BookingDto> getBookings(Integer userId, String state);

    List<BookingDto> getOwnersBookings(Integer userId, String state);

    List<BookingDto> getBookingsPagged(Integer userId, String state, Integer from, Integer size);

    List<BookingDto> getOwnersBookingsPagged(Integer userId, String state, Integer from, Integer size);
}
