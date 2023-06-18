package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ItemDto {
    private Integer id;
    @NotBlank
    @NotNull
    private String name;
    @NotBlank
    @NotNull
    private String description;
    @NotNull
    @JsonProperty("available")
    private Boolean isFree;
    private Integer requestId;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments = new ArrayList<>();
    private User user;

    public ItemDto() {

    }
}
