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
        userDao.getUserById(userId);
        return itemDao.addItem(item, userId);
    }

    public Item getItemById(long itemId) {
        return itemDao.getItemById(itemId);
    }

    public Item updateItem(Item item, long userId, long itemId) {
        if (itemDao.getItemById(itemId).getOwner() != userId) {
            throw new NotFoundException("User not exist");
        }
        return itemDao.updateItem(item, userId, itemId);
    }

    public void deleteItem(long itemId) {
        itemDao.deleteItem(itemId);
    }

    public List<Item> getItemsByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemDao.getItemsByText(text);
    }

    public List<Item> getAllItemsByUserId(long userId) {
        return itemDao.getAllItemsByUserId(userId);
    }
}
