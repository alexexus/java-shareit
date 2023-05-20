package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void toUserDto() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("mail@mail.ru")
                .build();
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("mail@mail.ru")
                .build();

        assertEquals(userDto, userMapper.toUserDto(user));
    }

    @Test
    void toUser() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("mail@mail.ru")
                .build();
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("mail@mail.ru")
                .build();

        assertEquals(user, userMapper.toUser(userDto));
    }
}
