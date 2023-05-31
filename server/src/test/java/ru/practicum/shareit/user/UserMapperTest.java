package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
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
