package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequestFrontDto that = (ItemRequestFrontDto) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(created, that.created) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, created, items);
    }
}
