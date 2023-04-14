package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public User addUser(User user) {
        checkEmail(user);
        return userDao.addUser(user);
    }

    public User getUserById(long id) {
        return userDao.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User updateUser(User user) {
        getUserById(user.getId());
        checkEmail(user);
        return userDao.updateUser(user);
    }

    public void deleteUser(long id) {
        getUserById(id);
        userDao.deleteUser(id);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    private void checkEmail(User userToCheck) {
        if (userDao.getAllUsers().stream()
                .filter(user -> !Objects.equals(user.getId(), userToCheck.getId()))
                .anyMatch(user -> user.getEmail().equals(userToCheck.getEmail()))) {
            throw new DuplicateException("Email already exist");
        }
    }
}
