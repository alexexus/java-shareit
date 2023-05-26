package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    Item addItem(Item item, long userId);

    Item getItemById(long itemId, long userId);

    Item updateItem(Item item, long userId, long itemId);

    void deleteItem(long itemId);

    List<Item> getItemsByText(String text, Integer from, Integer size);

    List<Item> getAllItemsByUserId(long userId, Integer from, Integer size);

    Comment addComment(Comment comment, long userId, long itemId);
}
