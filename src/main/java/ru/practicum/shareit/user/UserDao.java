package ru.practicum.shareit.user;

import java.util.List;

public interface UserDao {

    User addUser(User user);
    User getUserById(long id);
    User updateUser(User user);
    void deleteUser(long id);
    List<User> getAllUsers();
}
