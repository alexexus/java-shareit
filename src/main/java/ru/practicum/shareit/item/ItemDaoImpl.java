package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemDaoImpl implements ItemDao {

    private final Map<Long, Item> items = new HashMap<>();
    private long generateId = 0;

    @Override
    public Item addItem(Item item, long userId) {
        generateId++;
        item.setId(generateId);
        item.setOwner(userId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItemById(long itemId) {
        return items.get(itemId);
    }

    @Override
    public Item updateItem(Item item, long userId, long itemId) {
        if (item.getName() == null) {
            item.setName(items.get(itemId).getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(items.get(itemId).getDescription());
        }
        if (item.getId() == null) {
            item.setId(items.get(itemId).getId());
        }
        if (item.getOwner() == null) {
            item.setOwner(items.get(itemId).getOwner());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(items.get(itemId).getAvailable());
        }
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void deleteItem(long itemId) {
        items.remove(itemId);
    }

    @Override
    public List<Item> getItemsByText(String text) {
        String lowerCaseText = text.toLowerCase();
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(lowerCaseText) ||
                        item.getDescription().toLowerCase().contains(lowerCaseText))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner() == userId)
                .collect(Collectors.toList());
    }
}
