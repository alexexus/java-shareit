package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User addUser(User user);

    Optional<User> getUserById(long id);

    User updateUser(User user);

    void deleteUser(long id);

    List<User> getAllUsers();
}
