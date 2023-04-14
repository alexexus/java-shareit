package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserDao;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemDao itemDao;
    private final UserDao userDao;

    public Item addItem(Item item, long userId) {
        userDao.getUserById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return itemDao.addItem(item, userId);
    }

    public Item getItemById(long itemId) {
        return itemDao.getItemById(itemId).orElseThrow(() -> new NotFoundException("Item not found"));
    }

    public Item updateItem(Item item, long userId, long itemId) {
        getItemById(itemId);
        if (getItemById(itemId).getOwner() != userId) {
            throw new NotFoundException("User not exist");
        }
        return itemDao.updateItem(item, userId, itemId);
    }

    public void deleteItem(long itemId) {
        getItemById(itemId);
        itemDao.deleteItem(itemId);
    }

    public List<Item> getItemsByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemDao.getItemsByText(text);
    }

    public List<Item> getAllItemsByUserId(long userId) {
        userDao.getUserById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return itemDao.getAllItemsByUserId(userId);
    }
}
