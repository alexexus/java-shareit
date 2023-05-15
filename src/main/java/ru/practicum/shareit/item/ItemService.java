package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;

import java.util.List;

public interface ItemService {
    Item addItem(Item item, long userId);

    Item getItemById(long itemId, long userId);

    Item updateItem(Item item, long userId, long itemId);

    void deleteItem(long itemId);

    List<Item> getItemsByText(String text);

    List<Item> getAllItemsByUserId(long userId);

    Comment addComment(Comment comment, long userId, long itemId);
}
