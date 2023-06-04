package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ItemRequestFrontDto {

    private Integer id;

    private String description;

    private LocalDateTime created;

    private List<ItemDto> items = new ArrayList<>();

    public ItemRequestFrontDto() {

    }
}
