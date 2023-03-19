package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item createItem(ItemDto itemDto, Integer userId);

    Item updateItem(ItemDto itemDto, Long userId, Integer itemId);

    Item getItem(Long userId, Integer itemId);

    void removeItem(Long userId, Integer itemId);

    List<Item> getUsersItems(Long userId);

    List<Item> getItemByQuery(Long userId, String text);

}
