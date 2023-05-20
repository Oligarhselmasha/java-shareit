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
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {


    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public Booking createBooking(BookingDto bookingDto, Integer userId) {
        Booking booking = bookingMapper.toBooking(bookingDto);
        Item bookingsItem = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> {
            throw new MissingException("is not exist");
        });
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
        User bookingsUser = userRepository.findById(userId).orElseThrow(() -> {
            throw new MissingException("is not exist");
        });
        booking.setUser(bookingsUser);
        booking.setStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Integer userId, Integer bookingId, Boolean isApproved) {
        Booking bookingForUpdate = getBooking(bookingId);
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
        return bookingRepository.save(bookingForUpdate);
    }

    @Override
    public Booking getBooking(Integer bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> {
            throw new MissingException("is not exist");
        });
    }


    @Override
    public Booking getBooking(Integer bookingId, Integer userId) {
        Booking bookingForReceive = bookingRepository.findById(bookingId).orElseThrow(() -> {
            throw new MissingException("is not exist");
        });
        if (bookingForReceive.getUser().getId() != userId && bookingForReceive.getItem().getUser().getId() != userId) {
            throw new MissingException("thing isn't users thing");
        }
        return bookingForReceive;
    }


    @Override
    public List<Booking> getBookings(Integer userId, @NotNull String state) {
        userRepository.findById(userId).orElseThrow(() -> {
            throw new MissingException("is not exist");
        });
        switch (state) {
            case "ALL":
                return bookingRepository.findByUser_IdOrderByIdDesc(userId);
            case "CURRENT":
                return bookingRepository.findByUser_IdAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now());
            case "FUTURE":
                return bookingRepository.findByUser_IdAndStartTimeAfterOrderByIdDesc(userId, LocalDateTime.now());
            case "PAST":
                return bookingRepository.findByUser_IdAndEndTimeBeforeOrderByIdDesc(userId, LocalDateTime.now());
            case "WAITING":
                return bookingRepository.findByUser_IdAndStatusOrderByIdDesc(userId, BookingStatus.WAITING);
            case "REJECTED":
                return bookingRepository.findByUser_IdAndStatusOrderByIdDesc(userId, BookingStatus.REJECTED);
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<Booking> getOwnersBookings(Integer userId, String state) {
        userRepository.findById(userId).orElseThrow(() -> {
            throw new MissingException("is not exist");
        });
        switch (state) {
            case "ALL":
                return bookingRepository.findByItem_User_IdOrderByBookingIdDesc(userId);
            case "CURRENT":
                return bookingRepository.findByItem_User_IdAndStartTimeBeforeAndEndTimeAfterOrderByBookingIdDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now());
            case "FUTURE":
                return bookingRepository.findByItem_User_IdAndStartTimeAfterOrderByBookingIdDesc(userId,
                        LocalDateTime.now());
            case "PAST":
                return bookingRepository.findByItem_User_IdAndEndTimeBeforeOrderByBookingIdDesc(userId,
                        LocalDateTime.now());
            case "WAITING":
                return bookingRepository.findByItem_User_IdAndStatusOrderByBookingIdDesc(userId, BookingStatus.WAITING);
            case "REJECTED":
                return bookingRepository.findByItem_User_IdAndStatusOrderByBookingIdDesc(userId, BookingStatus.REJECTED);
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
