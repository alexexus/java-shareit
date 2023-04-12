package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {

    Item addItem(Item item, long userId);

    Item getItemById(long itemId);

    Item updateItem(Item item, long userId, long itemId);

    void deleteItem(long itemId);

    List<Item> getItemsByText(String text);

    List<Item> getAllItemsByUserId(long userId);
}
