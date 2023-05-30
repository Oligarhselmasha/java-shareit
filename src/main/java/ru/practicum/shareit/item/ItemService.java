package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, Integer userId);

    ItemDto updateItem(ItemDto itemDto, Integer userId, Integer itemId);

    ItemDto getItem(Integer itemId, Integer userId);

    void removeItem(Integer userId, Integer itemId);

    List<ItemDto> getUsersItems(Integer userId);

    List<ItemDto> getItemByQuery(Long userId, String text);

    CommentDto createItemsComment(CommentDto commentDto, Integer userId, Integer itemId);
}
