package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    @JsonProperty("id")
    private Integer bookingId;

    @NotNull
    private Integer itemId;

    @NotNull
    @JsonProperty("start")
    private LocalDateTime startTime;

    @NotNull
    @JsonProperty("end")
    private LocalDateTime endTime;

    private Integer bookerId;

}
