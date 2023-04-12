package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

    private final Map<Long, User> users = new HashMap<>();
    private long generateId = 0;

    @Override
    public User addUser(User user) {
        generateId++;
        user.setId(generateId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User not exist");
        }
        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        if (user.getEmail() == null) {
            user.setEmail(users.get(user.getId()).getEmail());
        }
        if (user.getName() == null) {
            user.setName(users.get(user.getId()).getName());
        }
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public void deleteUser(long id) {
        users.remove(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
