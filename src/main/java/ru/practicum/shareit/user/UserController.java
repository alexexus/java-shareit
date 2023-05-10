package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.OnCreate;
import ru.practicum.shareit.OnUpdate;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @PostMapping
    public UserDto addUser(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        return UserMapper.toUserDto(userServiceImpl.addUser(UserMapper.toUser(userDto)));
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        return UserMapper.toUserDto(userServiceImpl.getUserById(userId));
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Validated(OnUpdate.class) @RequestBody UserDto userDto,
                              @PathVariable long userId) {
        userDto.setId(userId);
        return UserMapper.toUserDto(userServiceImpl.updateUser(UserMapper.toUser(userDto)));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userServiceImpl.deleteUser(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userServiceImpl.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
