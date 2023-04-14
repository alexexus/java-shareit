package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {

    Item addItem(Item item, long userId);

    Optional<Item> getItemById(long itemId);

    Item updateItem(Item item, long userId, long itemId);

    void deleteItem(long itemId);

    List<Item> getItemsByText(String text);

    List<Item> getAllItemsByUserId(long userId);
}
