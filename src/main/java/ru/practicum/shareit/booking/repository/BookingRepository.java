package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("select b from Booking b where b.user.id = ?1 order by b.startTime DESC")
    List<Booking> findByUser_IdOrderByIdDesc(int id);

    @Query("select b from Booking b where b.user.id = ?1 and b.startTime > ?2 order by b.startTime DESC")
    List<Booking> findByUser_IdAndStartTimeAfterOrderByIdDesc(int id, LocalDateTime time);

    @Query("select b from Booking b " +
            "where b.user.id = ?1 and b.startTime < ?2 and b.endTime > ?3 " +
            "order by b.startTime DESC")
    List<Booking> findByUser_IdAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(int id, LocalDateTime startTime,
                                                                                     LocalDateTime endTime);

    @Query("select b from Booking b where b.user.id = ?1 and b.endTime < ?2 order by b.startTime DESC")
    List<Booking> findByUser_IdAndEndTimeBeforeOrderByIdDesc(int id, LocalDateTime time);

    @Query("select b from Booking b where b.user.id = ?1 and b.status = ?2 order by b.startTime DESC")
    List<Booking> findByUser_IdAndStatusOrderByIdDesc(int id, BookingStatus status);

    @Query("select b from Booking b where b.item.user.id = ?1 order by b.startTime DESC")
    List<Booking> findByItem_User_IdOrderByBookingIdDesc(int id);

    @Query("select b from Booking b " +
            "where b.item.user.id = ?1 and b.startTime < ?2 and b.endTime > ?3 " +
            "order by b.startTime DESC")
    List<Booking> findByItem_User_IdAndStartTimeBeforeAndEndTimeAfterOrderByBookingIdDesc(int id,
                                                                        LocalDateTime startTime, LocalDateTime endTime);

    @Query("select b from Booking b where b.item.user.id = ?1 and b.startTime > ?2 order by b.startTime DESC")
    List<Booking> findByItem_User_IdAndStartTimeAfterOrderByBookingIdDesc(int id, LocalDateTime startTime);

    @Query("select b from Booking b where b.item.user.id = ?1 and b.endTime < ?2 order by b.startTime DESC")
    List<Booking> findByItem_User_IdAndEndTimeBeforeOrderByBookingIdDesc(int id, LocalDateTime endTime);

    @Query("select b from Booking b where b.item.user.id = ?1 and b.status = ?2 order by b.startTime DESC")
    List<Booking> findByItem_User_IdAndStatusOrderByBookingIdDesc(int id, BookingStatus status);

    @Query("select b from Booking b " +
            "where b.item.id = ?1  and b.startTime < ?2 and b.item.user.id = ?3 " +
            "order by b.endTime DESC")
    List<Booking> findByItem_IdAndStartTimeBeforeAndItem_User_IdOrderByEndTimeDesc(Integer id, LocalDateTime startTime,
                                                                                                               int id1);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 and b.startTime > ?2 and b.item.user.id = ?3 and b.status = ?4 " +
            "order by b.startTime")
    List<Booking> findByItem_IdAndStartTimeAfterAndItem_User_IdIdAndStatusOrderByStartTimeAsc(Integer id,
                                                         LocalDateTime startTime, int id1, BookingStatus bookingStatus);

    @Query("select (count(b) > 0) from Booking b " +
            "where b.item.id = ?1 and b.user.id = ?2 and b.status = ?3 and b.endTime < ?4")
    boolean existsByItem_IdAndUser_IdAndStatusAndEndTimeBefore(Integer id, int id1, BookingStatus status,
                                                                                        LocalDateTime endTime);

    @Query("select b from Booking b where b.item.user.id = ?1 and b.startTime > ?2 order by b.item.user.name DESC")
    List<Booking> findByItem_User_IdAndStartTimeAfterOrderByItem_User_NameDesc(int id, LocalDateTime startTime);


}
