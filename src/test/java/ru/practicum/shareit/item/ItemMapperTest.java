package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ItemMapperTest {

    @Autowired
    private ItemMapper itemMapper;

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
        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .lastBooking(BookingDtoItem.builder().id(1L).build())
                .nextBooking(BookingDtoItem.builder().id(2L).build())
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
        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(1L)
                .lastBooking(BookingDtoItem.builder().id(1L).build())
                .nextBooking(BookingDtoItem.builder().id(2L).build())
                .comments(List.of(CommentDto.builder().id(1L).build()))
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
