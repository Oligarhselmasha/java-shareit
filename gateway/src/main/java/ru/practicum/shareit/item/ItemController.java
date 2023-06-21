package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

import static ru.practicum.shareit.constants.Variables.USER_HEADER;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(USER_HEADER) Integer userId,
                                             @Valid @RequestBody ItemDto itemDto) {
        return itemClient.createItem(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createItemsComment(@RequestHeader(USER_HEADER) Integer userId,
                                                     @Valid @RequestBody CommentDto commentDto, @PathVariable("itemId") Integer itemId) {
        return itemClient.createItemsComment(commentDto, userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_HEADER) Integer userId,
                                             @RequestBody ItemDto itemDto, @PathVariable("itemId") Integer itemId) {
        return itemClient.updateItem(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(USER_HEADER) Integer userId, @PathVariable("itemId") Integer itemId) {
        return itemClient.getItem(itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public void removeItem(@RequestHeader(USER_HEADER) Integer userId,
                           @PathVariable("itemId") Integer itemId) {
        itemClient.removeItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getUsersItems(@RequestHeader(USER_HEADER) Integer userId) {
        return itemClient.getUsersItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemByQuery(@RequestHeader(USER_HEADER) Long userId,
                                                  @RequestParam String text) {
        return itemClient.getItemByQuery(userId, text);
    }
}
