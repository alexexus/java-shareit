package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User updateUser(User user) {
        User oldUser = getUserById(user.getId());
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            oldUser.setEmail(user.getEmail());
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            oldUser.setName(user.getName());
        }
        return userRepository.save(oldUser);
    }

    public void deleteUser(long id) {
        getUserById(id);
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
