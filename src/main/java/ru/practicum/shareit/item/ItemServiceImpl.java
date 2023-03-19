package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.MissingException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;

    @Override
    public Item createItem(ItemDto itemDto, Integer userId) {
        Item item = itemMapper.toItem(itemDto);
        User owner = userRepository.getUser(userId);
        item.setOwner(owner);
        return itemRepository.createItem(item);
    }

    @Override
    public Item updateItem(ItemDto itemDto, Long userId, Integer itemId) {
        if (itemDto.getId() == null) {
            itemDto.setId(itemId);
        }
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(itemRepository.getItem(itemId).getOwner());
        if (item.getOwner().getId() != userId) {
            throw new MissingException("Isn't user's thing");
        }
        return itemRepository.updateItem(item);
    }

    @Override
    public Item getItem(Long userId, Integer itemId) {
        return itemRepository.getItem(itemId);
    }

    @Override
    public void removeItem(Long userId, Integer itemId) {
        Item item = getItem(userId, itemId);
        if (item.getOwner().getId() != userId) {
            throw new ValidationException("Isn't user's thing");
        }
        itemRepository.removeItem(itemId);
    }

    @Override
    public List<Item> getUsersItems(Long userId) {
        return itemRepository.getUsersItems(userId);
    }

    @Override
    public List<Item> getItemByQuery(Long userId, String text) {
        return itemRepository.getItemByQuery(userId, text);
    }
}
