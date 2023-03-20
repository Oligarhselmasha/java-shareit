package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public Item createItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @Valid @RequestBody ItemDto itemDto) {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestBody ItemDto itemDto, @PathVariable("itemId") Integer itemId) {
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public Item getItem(@PathVariable("itemId") Integer itemId) {
        return itemService.getItem(itemId);
    }

    @DeleteMapping("/{itemId}")
    public void removeItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable("itemId") Integer itemId) {
        itemService.removeItem(userId, itemId);
    }

    @GetMapping
    public List<Item> getUsersItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getUsersItems(userId);
    }

    @GetMapping("/search")
    public List<Item> findItemByQuery(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @RequestParam(required = true) String text) {
        return itemService.getItemByQuery(userId, text);
    }
}
