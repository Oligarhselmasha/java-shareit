package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime startTime;

    @NotNull
    @JsonProperty("end")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime endTime;

    private Integer bookerId;

}
