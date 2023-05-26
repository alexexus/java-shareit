package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class BookingMapperTest {

    @InjectMocks
    private BookingMapper bookingMapper;

    @Test
    void toBooking() {
        User booker = User.builder()
                .id(1L)
                .name("name")
                .email("mail@mail.ru")
                .build();
        Item item = Item.builder()
                .id(1L)
                .comments(Collections.emptyList())
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2000, 1, 1, 0, 0))
                .end(LocalDateTime.of(2000, 1, 1, 1, 0))
                .status(BookingConstant.WAITING)
                .booker(booker)
                .itemId(item.getId())
                .build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2000, 1, 1, 0, 0))
                .end(LocalDateTime.of(2000, 1, 1, 1, 0))
                .item(item)
                .booker(booker)
                .status(BookingConstant.WAITING)
                .build();

        assertEquals(booking, bookingMapper.toBooking(bookingDto));
    }

    @Test
    void toBookingDtoItem() {
        User booker = User.builder()
                .id(1L)
                .name("name")
                .email("mail@mail.ru")
                .build();
        Item item = Item.builder()
                .id(1L)
                .comments(Collections.emptyList())
                .build();
        BookingDtoItem bookingDtoItem = BookingDtoItem.builder()
                .id(1L)
                .bookerId(booker.getId())
                .build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2000, 1, 1, 0, 0))
                .end(LocalDateTime.of(2000, 1, 1, 1, 0))
                .item(item)
                .booker(booker)
                .status(BookingConstant.WAITING)
                .build();

        assertEquals(bookingDtoItem, bookingMapper.toBookingDtoItem(booking));
    }

    @Test
    void toBookingDtoItemWhenBookingIsNull() {
        assertNull(bookingMapper.toBookingDtoItem(null));
    }
}
