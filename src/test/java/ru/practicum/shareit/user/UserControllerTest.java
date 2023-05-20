package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController controller;
    @Mock
    private UserService service;
    @Mock
    private UserMapper mapper;

    @Test
    void addUser() {
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
        when(mapper.toUser(any(UserDto.class))).thenReturn(user);
        when(mapper.toUserDto(any(User.class))).thenReturn(userDto);
        when(service.addUser(any(User.class))).thenReturn(user);

        UserDto actual = controller.addUser(userDto);

        assertEquals(actual, userDto);
        verify(service, times(1)).addUser(any(User.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void getUserById() {
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
        when(mapper.toUserDto(any(User.class))).thenReturn(userDto);
        when(service.getUserById(anyLong())).thenReturn(user);

        UserDto actual = controller.getUserById(1L);

        assertEquals(actual, userDto);
        verify(service, times(1)).getUserById(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void updateUser() {
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
        when(mapper.toUser(any(UserDto.class))).thenReturn(user);
        when(mapper.toUserDto(any(User.class))).thenReturn(userDto);
        when(service.updateUser(any(User.class))).thenReturn(user);

        UserDto actual = controller.updateUser(userDto, 1L);

        assertEquals(actual, userDto);
        verify(service, times(1)).updateUser(any(User.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteUser() {
        doNothing().when(service).deleteUser(anyLong());

        controller.deleteUser(999L);
        verify(service, times(1)).deleteUser(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllUsers() {
        when(service.getAllUsers()).thenReturn(Collections.emptyList());

        List<UserDto> users = controller.getAllUsers();

        assertEquals(0, users.size());
        verify(service, times(1)).getAllUsers();
        verifyNoMoreInteractions(service);
    }
}
