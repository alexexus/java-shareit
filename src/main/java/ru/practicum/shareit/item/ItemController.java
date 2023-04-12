package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
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
    public Item addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                        @Valid @RequestBody Item item) {
        return itemService.addItem(item, userId);
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                            @PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @RequestBody Item item,
                           @PathVariable long itemId) {
        return itemService.updateItem(item, userId, itemId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) {
        itemService.deleteItem(itemId);
    }

    @GetMapping("/search")
    public List<Item> getItemsByText(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestParam String text) {
        return itemService.getItemsByText(text);
    }

    @GetMapping
    public List<Item> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllItemsByUserId(userId);
    }
}
