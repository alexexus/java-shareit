package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingConstant;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
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
class BookingControllerIntegrationTest {

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
    void addBooking() {
        User user = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).owner(2L).build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.MAX.minusDays(1))
                .end(LocalDateTime.MAX.minusHours(1))
                .item(item)
                .booker(user)
                .status(BookingConstant.WAITING)
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.MAX.minusDays(1))
                .end(LocalDateTime.MAX.minusHours(1))
                .itemId(1L)
                .booker(user)
                .status(BookingConstant.WAITING)
                .build();
        when(bookingMapper.toBooking(any(BookingDto.class))).thenReturn(booking);
        when(bookingService.addBooking(any(Booking.class), anyLong())).thenReturn(booking);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(booking), result);
    }

    @SneakyThrows
    @Test
    void getBookingById() {
        long bookerId = 1L;

        mockMvc.perform(get("/bookings/{bookingId}", bookerId)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(bookingService).getBookingById(1L, 1L);
    }

    @SneakyThrows
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
        when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(booking);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(booking), result);
    }

    @SneakyThrows
    @Test
    void deleteBooking() {
        doNothing().when(bookingService).deleteBooking(anyLong(), anyLong());

        mockMvc.perform(delete("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAllBookingsByUserId() {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "20")
                        .param("state", "ALL"))
                .andExpect(status().isOk());

        verify(bookingService).getAllBookingsByBookerIdAndState(1L, "ALL", 0, 20);
    }

    @SneakyThrows
    @Test
    void getAllBookingsByUserIdWithIllegalArgument() {
        when(bookingService.getAllBookingsByBookerIdAndState(anyLong(), anyString(), anyInt(), anyInt()))
                .thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "20")
                        .param("state", "UNKNOWN"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAllBookingsByState() {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "20")
                        .param("state", "ALL"))
                .andExpect(status().isOk());

        verify(bookingService).getAllBookingsByOwnerIdAndState(1L, "ALL", 0, 20);
    }
}
