package ru.practicum.shareit.item.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

@Data
@Component
public class ItemMapperImpl implements ItemMapper {

    private final ItemRepository itemRepository;

    @Override
    public Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .description(itemDto.getDescription())
                .isFree(itemDto.getIsFree())
                .name(itemDto.getName())
                .build();
    }

    @Override
    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .isFree(item.getIsFree())
                .build();
    }
}
