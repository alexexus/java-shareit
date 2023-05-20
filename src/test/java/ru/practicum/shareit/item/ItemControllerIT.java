package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ItemControllerIT {

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
    void addItem() {
        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .requestId(1L)
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .requestId(1L)
                .build();
        when(itemMapper.toItem(any(ItemDto.class))).thenReturn(item);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(itemService.addItem(any(Item.class), anyLong())).thenReturn(item);

        String result = mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void getItemById() {
        long itemId = 1L;

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(itemService).getItemById(anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void updateItem() {
        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .requestId(1L)
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("updateName")
                .description("description")
                .available(true)
                .owner(1L)
                .requestId(1L)
                .build();
        when(itemMapper.toItem(any(ItemDto.class))).thenReturn(item);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(itemService.updateItem(any(Item.class), anyLong(), anyLong())).thenReturn(item);

        String result = mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void updateItemUserNotOwner() {
        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .requestId(1L)
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("updateName")
                .description("description")
                .available(true)
                .owner(1L)
                .requestId(1L)
                .build();
        when(itemMapper.toItem(any(ItemDto.class))).thenReturn(item);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(itemService.updateItem(any(Item.class), anyLong(), anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void deleteItem() {
        doNothing().when(itemService).deleteItem(anyLong());

        mockMvc.perform(delete("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getItemsByText() {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "20")
                        .param("text", "text"))
                .andExpect(status().isOk());

        verify(itemService).getItemsByText(anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getItemsByTextNotValidFrom() {
        when(itemService.getItemsByText(anyString(), anyInt(), anyInt())).thenThrow(ValidationException.class);

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "-1")
                        .param("size", "20")
                        .param("text", "text"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getItemsByTextInternalServerError() {
        mockMvc.perform(get("/items/searchByText")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "20")
                        .param("text", "text"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAllItemsByUserId() {
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "20"))
                .andExpect(status().isOk());

        verify(itemService).getAllItemsByUserId(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void addComment() {
        User user = User.builder().id(1L).name("name").build();
        Item item = Item.builder().build();
        Comment comment = Comment.builder()
                .id(1L)
                .text("text")
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build();
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("text")
                .authorName("name")
                .created(LocalDateTime.now())
                .build();
        when(commentMapper.toCommentDto(any(Comment.class))).thenReturn(commentDto);
        when(itemService.addComment(any(Comment.class), anyLong(), anyLong())).thenReturn(comment);

        String result = mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentDto), result);
    }
}
