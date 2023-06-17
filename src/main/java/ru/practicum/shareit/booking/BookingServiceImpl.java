package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.MissingException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {


    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto createBooking(BookingDto bookingDto, Integer userId) {
        Booking booking = bookingMapper.toBooking(bookingDto);
        Item bookingsItem = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new MissingException("is not exist"));
        if (bookingsItem.getUser().getId() == userId) {
            throw new MissingException("is bookers item");
        }
        if (booking.getStartTime().isAfter(booking.getEndTime()) ||
                booking.getStartTime().isEqual(booking.getEndTime()) ||
                booking.getStartTime().isBefore(LocalDateTime.now()) ||
                booking.getEndTime().isBefore(LocalDateTime.now())) {
            throw new ValidationException("wrong date");
        }
        if (!bookingsItem.getIsFree()) {
            throw new ValidationException("thing isn't available");
        }
        booking.setItem(bookingsItem);
        User bookingsUser = userRepository.findById(userId).orElseThrow(() ->
                new MissingException("is not exist"));
        booking.setUser(bookingsUser);
        booking.setStatus(BookingStatus.WAITING);
        Booking bookingForReceive = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(bookingForReceive);
    }

    @Override
    public BookingDto updateBooking(Integer userId, Integer bookingId, Boolean isApproved) {
        Booking bookingForUpdate = bookingMapper.toBooking(getBooking(bookingId));
        if (bookingForUpdate.getItem().getUser().getId() != userId) {
            throw new MissingException("thing isn't users thing");
        }
        if (isApproved) {
            if (bookingForUpdate.getStatus().equals(BookingStatus.APPROVED)) {
                throw new ValidationException("Status already exist");
            }
            bookingForUpdate.setStatus(BookingStatus.APPROVED);
        } else {
            if (bookingForUpdate.getStatus().equals(BookingStatus.REJECTED)) {
                throw new ValidationException("Status already exist");
            }
            bookingForUpdate.setStatus(BookingStatus.REJECTED);
        }
        Booking bookingForReceive = bookingRepository.save(bookingForUpdate);
        return bookingMapper.toBookingDto(bookingForReceive);
    }

    @Override
    public BookingDto getBooking(Integer bookingId) {
        Booking bookingForReceive = bookingRepository.findById(bookingId).orElseThrow(() ->
                new MissingException("is not exist"));
        return bookingMapper.toBookingDto(bookingForReceive);
    }


    @Override
    public BookingDto getBooking(Integer bookingId, Integer userId) {
        Booking bookingForReceive = bookingRepository.findById(bookingId).orElseThrow(() -> {
            throw new MissingException("is not exist");
        });
        if (bookingForReceive.getUser().getId() != userId && bookingForReceive.getItem().getUser().getId() != userId) {
            throw new MissingException("thing isn't users thing");
        }
        return bookingMapper.toBookingDto(bookingForReceive);
    }


    @Override
    public List<BookingDto> getBookings(Integer userId, @NotNull String state) {
        userRepository.findById(userId).orElseThrow(() ->
                new MissingException("is not exist"));
        switch (state) {
            case "ALL":
                return convertBookingsList(bookingRepository.findByUser_IdOrderByIdDesc(userId));
            case "CURRENT":
                return convertBookingsList(bookingRepository.findByUser_IdAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now()));
            case "FUTURE":
                return convertBookingsList(bookingRepository.findByUser_IdAndStartTimeAfterOrderByIdDesc(userId, LocalDateTime.now()));
            case "PAST":
                return convertBookingsList(bookingRepository.findByUser_IdAndEndTimeBeforeOrderByIdDesc(userId, LocalDateTime.now()));
            case "WAITING":
                return convertBookingsList(bookingRepository.findByUser_IdAndStatusOrderByIdDesc(userId, BookingStatus.WAITING));
            case "REJECTED":
                return convertBookingsList(bookingRepository.findByUser_IdAndStatusOrderByIdDesc(userId, BookingStatus.REJECTED));
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingDto> getOwnersBookings(Integer userId, String state) {
        userRepository.findById(userId).orElseThrow(() -> {
            throw new MissingException("is not exist");
        });
        switch (state) {
            case "ALL":
                return convertBookingsList(bookingRepository.findByItem_User_IdOrderByBookingIdDesc(userId));
            case "CURRENT":
                return convertBookingsList(bookingRepository.findByItem_User_IdAndStartTimeBeforeAndEndTimeAfterOrderByBookingIdDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now()));
            case "FUTURE":
                return convertBookingsList(bookingRepository.findByItem_User_IdAndStartTimeAfterOrderByBookingIdDesc(userId,
                        LocalDateTime.now()));
            case "PAST":
                return convertBookingsList(bookingRepository.findByItem_User_IdAndEndTimeBeforeOrderByBookingIdDesc(userId,
                        LocalDateTime.now()));
            case "WAITING":
                return convertBookingsList(bookingRepository.findByItem_User_IdAndStatusOrderByBookingIdDesc(userId, BookingStatus.WAITING));
            case "REJECTED":
                return convertBookingsList(bookingRepository.findByItem_User_IdAndStatusOrderByBookingIdDesc(userId, BookingStatus.REJECTED));
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingDto> getBookingsPagged(Integer userId, String state, Integer from, Integer size) {
        List<BookingDto> bookings = getBookings(userId, state);
        return makePagging(bookings, from, size);
    }

    @Override
    public List<BookingDto> getOwnersBookingsPagged(Integer userId, String state, Integer from, Integer size) {
        List<BookingDto> bookings = getOwnersBookings(userId, state);
        return makePagging(bookings, from, size);
    }

    private List<BookingDto> makePagging(List<BookingDto> bookings, Integer from, Integer size) {
        if (from == 0 && size == 1) {
            return bookings;
        }
        if (from < 0 || size <= 0) {
            throw new ValidationException("wrong parameters");
        }
        List<BookingDto> bookingForReceive;
        if (from + size >= bookings.size()) {
            size = bookings.size();
            bookingForReceive = bookings.subList(from, size);
            return bookingForReceive;
        }
        bookingForReceive = bookings.subList(from, from + size);
        return bookingForReceive;
    }

    private List<BookingDto> convertBookingsList(List<Booking> bookings) {
        List<BookingDto> bookingDtos = new ArrayList<>();
        bookings.forEach(b -> bookingDtos.add(bookingMapper.toBookingDto(b)));
        return bookingDtos;
    }
}
