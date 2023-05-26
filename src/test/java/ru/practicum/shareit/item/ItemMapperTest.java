package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @InjectMocks
    private ItemMapper itemMapper;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private CommentMapper commentMapper;

    @Test
    void toItemDto() {
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

        assertEquals(itemDto, itemMapper.toItemDto(item));
    }

    @Test
    void toItem() {
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

        assertEquals(item, itemMapper.toItem(itemDto));
    }

    @Test
    void toItemDtoWithoutRequestId() {
        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .build();

        assertEquals(itemDto, itemMapper.toItemDto(item));
    }

    @Test
    void toItemWithoutRequestId() {
        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .build();

        assertEquals(item, itemMapper.toItem(itemDto));
    }

    @Test
    void toItemDtoWithBookings() {
        when(bookingMapper.toBookingDtoItem(Booking.builder().id(1L).build()))
                .thenReturn(BookingDtoItem.builder().id(1L).build());
        when(bookingMapper.toBookingDtoItem(Booking.builder().id(2L).build()))
                .thenReturn(BookingDtoItem.builder().id(2L).build());

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .lastBooking(Booking.builder().id(1L).build())
                .nextBooking(Booking.builder().id(2L).build())
                .build();
        ItemDtoWithBookings itemDtoWithBookings = ItemDtoWithBookings.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .lastBooking(BookingDtoItem.builder().id(1L).build())
                .nextBooking(BookingDtoItem.builder().id(2L).build())
                .build();

        assertEquals(itemDtoWithBookings, itemMapper.toItemDtoWithBookings(item));
    }

    @Test
    void toItemDtoWithComments() {
        when(bookingMapper.toBookingDtoItem(Booking.builder().id(1L).build()))
                .thenReturn(BookingDtoItem.builder().id(1L).build());
        when(bookingMapper.toBookingDtoItem(Booking.builder().id(2L).build()))
                .thenReturn(BookingDtoItem.builder().id(2L).build());
        when(commentMapper.toCommentDto(any(Comment.class)))
                .thenReturn(CommentDto.builder().id(1L).build());

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .lastBooking(Booking.builder().id(1L).build())
                .nextBooking(Booking.builder().id(2L).build())
                .comments(List.of(Comment.builder().id(1L).build()))
                .build();
        ItemDtoWithComments itemDtoWithComments = ItemDtoWithComments.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .lastBooking(BookingDtoItem.builder().id(1L).build())
                .nextBooking(BookingDtoItem.builder().id(2L).build())
                .comments(List.of(CommentDto.builder().id(1L).build()))
                .build();

        assertEquals(itemDtoWithComments, itemMapper.toItemDtoWithComments(item));
    }
}
