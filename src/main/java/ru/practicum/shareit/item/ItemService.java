package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item createItem(ItemDto itemDto, Integer userId);

    Item updateItem(ItemDto itemDto, Integer userId, Integer itemId);

    Item getItem(Integer itemId, Integer userId);

    void removeItem(Integer userId, Integer itemId);

    List<Item> getUsersItems(Integer userId);

    List<Item> getItemByQuery(Long userId, String text);

    CommentDto createItemsComment(CommentDto commentDto, Integer userId, Integer itemId);
}
