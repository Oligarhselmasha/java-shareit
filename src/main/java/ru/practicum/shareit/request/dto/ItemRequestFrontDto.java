package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class ItemRequestFrontDto {

    private Integer id;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime created;

    private List<ItemDto> items = new ArrayList<>();

    public ItemRequestFrontDto() {

    }
}
