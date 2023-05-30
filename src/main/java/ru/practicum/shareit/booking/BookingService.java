package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    Booking createBooking(BookingDto bookingDto, Integer userId);

    Booking updateBooking(Integer userId, Integer bookingId, Boolean isApproved);

    Booking getBooking(Integer bookingId);

    Booking getBooking(Integer bookingId, Integer userId);

    List<Booking> getBookings(Integer userId, String state);

    List<Booking> getOwnersBookings(Integer userId, String state);

    List<Booking> getBookingsPagged(Integer userId, String state, String from, String size);

    List<Booking> getOwnersBookingsPagged(Integer userId, String state, String from, String size);
}
