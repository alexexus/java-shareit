package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.OnCreate;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    public static final String USER_ID = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(USER_ID) Long userId,
                                          @Validated(OnCreate.class) @RequestBody ItemDto itemDto) {
        return itemClient.addItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(USER_ID) Long userId,
                                              @PathVariable Long itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_ID) Long userId,
                                             @RequestBody ItemDto itemDto,
                                             @PathVariable Long itemId) {
        return itemClient.updateItem(userId, itemDto, itemId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader(USER_ID) Long userId,
                                             @PathVariable Long itemId) {
        return itemClient.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByText(@RequestHeader(USER_ID) Long userId,
                                                 @RequestParam String text,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemClient.getItemsByText(userId, text, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUserId(@RequestHeader(USER_ID) Long userId,
                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemClient.getAllItemsByUserId(userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(USER_ID) Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody CommentDto commentDto) {
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
