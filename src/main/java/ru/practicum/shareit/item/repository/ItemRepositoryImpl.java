package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.MissingException;
import ru.practicum.shareit.item.model.Item;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private int itemId = 0;
    private final HashMap<Integer, Item> items = new HashMap<>();


    @Override
    public Item createItem(Item item) {
        itemId++;
        item.setId(itemId);
        items.put(itemId, item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        Item itemUpdated = getItem(item.getId());
        if (item.getDescription() != null) {
            itemUpdated.setDescription(item.getDescription());
        }
        if (item.getName() != null) {
            itemUpdated.setName(item.getName());
        }
        if (item.getIsFree() != null) {
            itemUpdated.setIsFree(item.getIsFree());
        }
        removeItem(item.getId());
        items.put(itemUpdated.getId(), itemUpdated);
        return itemUpdated;
    }

    @Override
    public Item getItem(Integer itemId) {
        return items.values().stream()
                .filter(item -> Objects.equals(item.getId(), itemId))
                .findFirst()
                .orElseThrow(() -> new MissingException("No found with id: " + itemId));
    }

    @Override
    public void removeItem(Integer itemId) {
        items.values().stream()
                .filter(item -> Objects.equals(item.getId(), itemId))
                .findFirst()
                .orElseThrow(() -> new MissingException("No found with id: " + itemId));
        items.remove(itemId);
    }

    @Override
    public List<Item> getUsersItems(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemByQuery(Long userId, String text) {
        if (text.isBlank() || text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> freeItems = items.values().stream()
                .filter(item -> item.getIsFree().equals(true))
                .collect(Collectors.toList());
        return freeItems.stream()
                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                        item.getName().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }
}
