package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private BookingMapper bookingMapper;
    @MockBean
    private ItemService itemService;
    @MockBean
    private ItemMapper itemMapper;
    @MockBean
    private CommentMapper commentMapper;
    @MockBean
    private ItemRequestService itemRequestService;
    @MockBean
    private ItemRequestMapper itemRequestMapper;


    @SneakyThrows
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
        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);
        when(userService.addUser(any(User.class))).thenReturn(user);

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void addUserNotValidMail() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("notValidMail")
                .build();

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getUserById() {
        long userId = 1L;

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userService).getUserById(userId);
    }

    @SneakyThrows
    @Test
    void updateUser() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("mail@mail.ru")
                .build();
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("updateName")
                .email("mail@mail.ru")
                .build();
        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);
        when(userService.updateUser(any(User.class))).thenReturn(user);

        String result = mockMvc.perform(patch("/users/{userId}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void deleteUser() {
        doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/users/{userId}", anyLong()))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAllUsers() {
        when(userMapper.toUser(any(UserDto.class))).thenReturn(new User());
        when(userMapper.toUserDto(any(User.class))).thenReturn(new UserDto());
        when(userService.getAllUsers()).thenReturn(List.of(new User(), new User()));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }
}
