package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @InjectMocks
    private BookingController controller;
    @Mock
    private BookingService service;
    @Mock
    private BookingMapper mapper;

    @Test
    void addBooking() {
        User user = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).owner(2L).build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2000, 1, 1, 0, 0))
                .end(LocalDateTime.of(2000, 1, 1, 1, 0))
                .item(item)
                .booker(user)
                .status(BookingConstant.WAITING)
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2000, 1, 1, 0, 0))
                .end(LocalDateTime.of(2000, 1, 1, 1, 0))
                .itemId(1L)
                .booker(user)
                .status(BookingConstant.WAITING)
                .build();
        when(mapper.toBooking(any(BookingDto.class))).thenReturn(booking);
        when(service.addBooking(any(Booking.class), anyLong())).thenReturn(booking);

        Booking actual = controller.addBooking(1L, bookingDto);

        assertEquals(actual, booking);
        verify(service, times(1)).addBooking(booking, 1L);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getBookingById() {
        User user = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).owner(2L).build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2000, 1, 1, 0, 0))
                .end(LocalDateTime.of(2000, 1, 1, 1, 0))
                .item(item)
                .booker(user)
                .status(BookingConstant.WAITING)
                .build();
        when(service.getBookingById(anyLong(), anyLong())).thenReturn(booking);

        Booking actual = controller.getBookingById(1L, 1L);

        assertEquals(actual, booking);
        verify(service, times(1)).getBookingById(1L, 1L);
        verifyNoMoreInteractions(service);
    }

    @Test
    void updateBooking() {
        User user = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).owner(2L).build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2000, 1, 1, 0, 0))
                .end(LocalDateTime.of(2000, 1, 1, 1, 0))
                .item(item)
                .booker(user)
                .status(BookingConstant.WAITING)
                .build();
        when(service.updateBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(booking);

        Booking actual = controller.updateBooking(1L, 1L, true);

        assertEquals(actual, booking);
        verify(service, times(1)).updateBooking(1L, 1L, true);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteBooking() {
        doNothing().when(service).deleteBooking(anyLong(), anyLong());

        controller.deleteBooking(999L, 999L);
        verify(service, times(1)).deleteBooking(999L, 999L);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllBookingsByUserId() {
        when(service.getAllBookingsByBookerIdAndState(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        List<Booking> actual = controller.getAllBookingsByUserId(1L, "ALL", 0, 20);

        assertEquals(0, actual.size());
        verify(service, times(1))
                .getAllBookingsByBookerIdAndState(1L, "ALL", 0, 20);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllBookingsByState() {
        when(service.getAllBookingsByOwnerIdAndState(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        List<Booking> actual = controller.getAllBookingsByState(1L, "ALL", 0, 20);

        assertEquals(0, actual.size());
        verify(service, times(1))
                .getAllBookingsByOwnerIdAndState(1L, "ALL", 0, 20);
        verifyNoMoreInteractions(service);
    }
}
