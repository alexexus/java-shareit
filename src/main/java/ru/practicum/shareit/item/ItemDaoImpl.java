package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemDaoImpl implements ItemDao {

    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Item>> userItemIndex = new LinkedHashMap<>();
    private long generateId = 0;

    @Override
    public Item addItem(Item item, long userId) {
        generateId++;
        item.setId(generateId);
        item.setOwner(userId);
        items.put(item.getId(), item);
        final List<Item> userItems = userItemIndex.computeIfAbsent(item.getOwner(), k -> new ArrayList<>());
        userItems.add(item);
        return item;
    }

    @Override
    public Optional<Item> getItemById(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Item updateItem(Item item, long userId, long itemId) {
        Item oldItem = items.get(itemId);
        if (item.getName() != null && !item.getName().isBlank()) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getOwner() != null && item.getOwner() > 0) {
            oldItem.setOwner(item.getOwner());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        return oldItem;
    }

    @Override
    public void deleteItem(long itemId) {
        Item item = items.remove(itemId);
        userItemIndex.get(item.getOwner()).remove(item);
    }

    @Override
    public List<Item> getItemsByText(String text) {
        String lowerCaseText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getAvailable() && (item.getName().toLowerCase().contains(lowerCaseText) ||
                        item.getDescription().toLowerCase().contains(lowerCaseText)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId) {
        return userItemIndex.getOrDefault(userId, List.of());
    }
}
