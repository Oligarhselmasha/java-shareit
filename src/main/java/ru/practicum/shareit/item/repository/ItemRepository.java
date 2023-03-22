package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item createItem(Item item);

    Item updateItem(Item item);

    Item getItem(Integer itemId);

    void removeItem(Integer itemId);

    List<Item> getUsersItems(Long userId);

    List<Item> getItemByQuery(Long userId, String text);
}
