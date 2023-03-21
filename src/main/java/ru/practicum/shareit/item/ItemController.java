package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.constants.Variables.USER_HEADER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public Item createItem(@RequestHeader(USER_HEADER) Integer userId,
                           @Valid @RequestBody ItemDto itemDto) {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader(USER_HEADER) Long userId,
                           @RequestBody ItemDto itemDto, @PathVariable("itemId") Integer itemId) {
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public Item getItem(@PathVariable("itemId") Integer itemId) {
        return itemService.getItem(itemId);
    }

    @DeleteMapping("/{itemId}")
    public void removeItem(@RequestHeader(USER_HEADER) Long userId,
                           @PathVariable("itemId") Integer itemId) {
        itemService.removeItem(userId, itemId);
    }

    @GetMapping
    public List<Item> getUsersItems(@RequestHeader(USER_HEADER) Long userId) {
        return itemService.getUsersItems(userId);
    }

    @GetMapping("/search")
    public List<Item> findItemByQuery(@RequestHeader(USER_HEADER) Long userId,
                                      @RequestParam String text) {
        return itemService.getItemByQuery(userId, text);
    }
}
