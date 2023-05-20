package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @InjectMocks
    private ItemController controller;
    @Mock
    private ItemService service;
    @Mock
    private ItemMapper mapper;
    @Mock
    private CommentMapper commentMapper;

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
        when(mapper.toItem(any(ItemDto.class))).thenReturn(item);
        when(mapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(service.addItem(any(Item.class), anyLong())).thenReturn(item);

        ItemDto actual = controller.addItem(1L, itemDto);

        assertEquals(actual, itemDto);
        verify(service, times(1)).addItem(any(Item.class), anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getItemById() {
        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .lastBooking(BookingDtoItem.builder().build())
                .nextBooking(BookingDtoItem.builder().build())
                .comments(Collections.emptyList())
                .build();
        ItemDtoWithComments itemDtoWithComments = ItemDtoWithComments.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .lastBooking(BookingDtoItem.builder().build())
                .nextBooking(BookingDtoItem.builder().build())
                .comments(Collections.emptyList())
                .build();
        when(mapper.toItemDtoWithComments(any(Item.class))).thenReturn(itemDtoWithComments);
        when(service.getItemById(anyLong(), anyLong())).thenReturn(item);

        ItemDtoWithComments actual = controller.getItemById(1L, 1L);

        assertEquals(actual, itemDtoWithComments);
        verify(service, times(1)).getItemById(anyLong(), anyLong());
        verifyNoMoreInteractions(service);
    }

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
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .requestId(1L)
                .build();
        when(mapper.toItem(any(ItemDto.class))).thenReturn(item);
        when(mapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(service.updateItem(any(Item.class), anyLong(), anyLong())).thenReturn(item);

        ItemDto actual = controller.updateItem(1L, itemDto, 1L);

        assertEquals(actual, itemDto);
        verify(service, times(1)).updateItem(any(Item.class), anyLong(), anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteItem() {
        doNothing().when(service).deleteItem(anyLong());

        controller.deleteItem(999L, 999L);
        verify(service, times(1)).deleteItem(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getItemsByText() {
        when(service.getItemsByText(anyString(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        List<ItemDto> actual = controller.getItemsByText(1L, "text", 0, 20);

        assertEquals(0, actual.size());
        verify(service, times(1)).getItemsByText(anyString(), anyInt(), anyInt());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllItemsByUserId() {
        when(service.getAllItemsByUserId(anyLong(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        List<ItemDtoWithBookings> actual = controller.getAllItemsByUserId(1L, 0, 20);

        assertEquals(0, actual.size());
        verify(service, times(1)).getAllItemsByUserId(anyLong(), anyInt(), anyInt());
        verifyNoMoreInteractions(service);
    }

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
        when(service.addComment(any(Comment.class), anyLong(), anyLong())).thenReturn(comment);

        CommentDto actual = controller.addComment(1L, 1L, comment);

        assertEquals(actual, commentDto);
        verify(service, times(1)).addComment(any(Comment.class), anyLong(), anyLong());
        verifyNoMoreInteractions(service);
    }
}
