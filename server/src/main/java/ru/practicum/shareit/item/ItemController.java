package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

import static ru.practicum.shareit.constants.Variables.USER_HEADER;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(USER_HEADER) Integer userId,
                              @RequestBody ItemDto itemDto) {
        return itemService.createItem(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createItemsComment(@RequestHeader(USER_HEADER) Integer userId,
                                         @RequestBody CommentDto commentDto, @PathVariable("itemId") Integer itemId) {
        return itemService.createItemsComment(commentDto, userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_HEADER) Integer userId,
                              @RequestBody ItemDto itemDto, @PathVariable("itemId") Integer itemId) {
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@RequestHeader(USER_HEADER) Integer userId, @PathVariable("itemId") Integer itemId) {
        return itemService.getItem(itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public void removeItem(@RequestHeader(USER_HEADER) Integer userId,
                           @PathVariable("itemId") Integer itemId) {
        itemService.removeItem(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getUsersItems(@RequestHeader(USER_HEADER) Integer userId) {
        return itemService.getUsersItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemByQuery(@RequestHeader(USER_HEADER) Long userId,
                                         @RequestParam String text) {
        return itemService.getItemByQuery(userId, text);
    }
}
