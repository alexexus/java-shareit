package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.OnCreate;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    public static final String USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @PostMapping
    public ItemDto addItem(@RequestHeader(USER_ID) long userId,
                           @Validated(OnCreate.class) @RequestBody ItemDto itemDto) {
        return itemMapper.toItemDto(itemService.addItem(itemMapper.toItem(itemDto), userId));
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithComments getItemById(@RequestHeader(USER_ID) long userId,
                                           @PathVariable long itemId) {
        return itemMapper.toItemDtoWithComments(itemService.getItemById(itemId, userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_ID) long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable long itemId) {
        return itemMapper.toItemDto(itemService.updateItem(itemMapper.toItem(itemDto), userId, itemId));
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(USER_ID) long userId,
                           @PathVariable long itemId) {
        itemService.deleteItem(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByText(@RequestHeader(USER_ID) long userId,
                                        @RequestParam String text) {
        return itemService.getItemsByText(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<ItemDtoWithBookings> getAllItemsByUserId(@RequestHeader(USER_ID) long userId) {
        return itemService.getAllItemsByUserId(userId).stream()
                .map(itemMapper::toItemDtoWithBookings)
                .collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(USER_ID) long userId,
                                 @PathVariable long itemId,
                                 @RequestBody Comment comment) {
        return commentMapper.toCommentDto(itemService.addComment(comment, userId, itemId));
    }
}
