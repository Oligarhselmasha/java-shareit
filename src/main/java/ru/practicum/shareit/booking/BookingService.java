package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking createBooking(BookingDto bookingDto, Integer userId);

    Booking updateBooking(Integer userId, Integer bookingId, Boolean isApproved);

    Booking getBooking(Integer bookingId);

    Booking getBooking(Integer bookingId, Integer userId);

    List<Booking> getBookings(Integer userId, String state);

    List<Booking> getOwnersBookings(Integer userId, String state);
}
