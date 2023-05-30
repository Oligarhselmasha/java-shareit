package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "bookings")
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Integer bookingId;

    @Column
    @JsonProperty("start")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime startTime;

    @Column
    @JsonProperty("end")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    @ToString.Exclude
    private Item item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @JsonProperty("booker")
    private User user;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column
    private String comment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return bookingId != null && bookingId.equals(((Booking) o).getBookingId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }
}
