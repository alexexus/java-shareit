package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private BookingServiceImpl service;

    @Test
    void addBooking() {
        User user = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).available(true).owner(2L).build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(user)
                .status(BookingConstant.WAITING)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking actual = service.addBooking(booking, 1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(booking);
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void addBookingNotAvailableItem() {
        User user = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).available(false).owner(2L).build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(user)
                .status(BookingConstant.WAITING)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        Assertions.assertThrows(ValidationException.class, () -> service.addBooking(booking, 1L));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void addBookingNotValidEnd() {
        User user = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).available(true).owner(2L).build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(user)
                .status(BookingConstant.WAITING)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        Assertions.assertThrows(ValidationException.class, () -> service.addBooking(booking, 1L));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void addBookingBookerIsItemOwner() {
        User booker = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).available(true).owner(1L).build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingConstant.WAITING)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        Assertions.assertThrows(NotFoundException.class, () -> service.addBooking(booking, 1L));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getBookingById() {
        User user = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).available(true).owner(2L).build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(user)
                .status(BookingConstant.WAITING)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Booking actual = service.getBookingById(1L, 1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(booking);
        verify(bookingRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getBookingByIdUserNotBookerOrOwner() {
        User booker = User.builder().id(1L).build();
        User user = User.builder().id(3L).build();
        Item item = Item.builder().id(1L).available(true).owner(2L).build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(user)
                .status(BookingConstant.WAITING)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Assertions.assertThrows(NotFoundException.class, () -> service.getBookingById(1L, 1L));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void updateBookingApproved() {
        User user = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).available(true).owner(1L).build();
        Booking bookingToUpdate = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(user)
                .status(BookingConstant.WAITING)
                .build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(user)
                .status(BookingConstant.APPROVED)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingToUpdate));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking actual = service.updateBooking(1L, 1L, true);

        assertThat(actual).usingRecursiveComparison().isEqualTo(booking);
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void updateBookingUserNotOwner() {
        User user = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).available(true).owner(2L).build();
        Booking bookingToUpdate = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(user)
                .status(BookingConstant.WAITING)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingToUpdate));

        Assertions.assertThrows(NotFoundException.class, () -> service.updateBooking(1L, 1L, true));

        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void updateBookingNotValidStatus() {
        User user = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).available(true).owner(1L).build();
        Booking bookingToUpdate = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(user)
                .status(BookingConstant.APPROVED)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingToUpdate));

        Assertions.assertThrows(ValidationException.class, () -> service.updateBooking(1L, 1L, true));

        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void updateBookingRejected() {
        User user = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).available(true).owner(1L).build();
        Booking bookingToUpdate = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(user)
                .status(BookingConstant.WAITING)
                .build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(user)
                .status(BookingConstant.REJECTED)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingToUpdate));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking actual = service.updateBooking(1L, 1L, false);

        assertThat(actual).usingRecursiveComparison().isEqualTo(booking);
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void deleteBooking() {
        User user = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).available(true).owner(1L).build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(user)
                .status(BookingConstant.WAITING)
                .build();
        doNothing().when(bookingRepository).deleteById(anyLong());
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        service.deleteBooking(1L, 1L);

        verify(bookingRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getAllBookingsByBookerIdAndState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findByBookerIdOrderByStartDesc(anyLong(), any(PageRequest.class)))
                .thenReturn(Collections.emptyList());

        service.getAllBookingsByBookerIdAndState(1L, "ALL", 0, 20);
        service.getAllBookingsByBookerIdAndState(1L, "PAST", 0, 20);
        service.getAllBookingsByBookerIdAndState(1L, "FUTURE", 0, 20);
        service.getAllBookingsByBookerIdAndState(1L, "CURRENT", 0, 20);
        service.getAllBookingsByBookerIdAndState(1L, "WAITING", 0, 20);
        service.getAllBookingsByBookerIdAndState(1L, "REJECTED", 0, 20);

        verify(bookingRepository, times(1))
                .findByBookerIdOrderByStartDesc(anyLong(), any(PageRequest.class));
        verify(bookingRepository, times(1))
                .findByBookerIdAndEndIsBeforeOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class),
                        any(PageRequest.class));
        verify(bookingRepository, times(1))
                .findByBookerIdAndEndIsAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class),
                        any(PageRequest.class));
        verify(bookingRepository, times(1))
                .findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class),
                        any(PageRequest.class));
        verify(bookingRepository, times(2))
                .findByBookerIdAndStatusIsOrderByStartDesc(anyLong(),
                        any(BookingConstant.class),
                        any(PageRequest.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getAllBookingsByBookerIdAndStateWithoutFromAndSize() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findByBookerIdOrderByStartDesc(anyLong()))
                .thenReturn(Collections.emptyList());

        service.getAllBookingsByBookerIdAndState(1L, "ALL", null, null);
        service.getAllBookingsByBookerIdAndState(1L, "PAST", null, null);
        service.getAllBookingsByBookerIdAndState(1L, "FUTURE", null, null);
        service.getAllBookingsByBookerIdAndState(1L, "CURRENT", null, null);
        service.getAllBookingsByBookerIdAndState(1L, "WAITING", null, null);
        service.getAllBookingsByBookerIdAndState(1L, "REJECTED", null, null);

        verify(bookingRepository, times(1))
                .findByBookerIdOrderByStartDesc(anyLong());
        verify(bookingRepository, times(1))
                .findByBookerIdAndEndIsBeforeOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class));
        verify(bookingRepository, times(1))
                .findByBookerIdAndEndIsAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class));
        verify(bookingRepository, times(1))
                .findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class));
        verify(bookingRepository, times(2))
                .findByBookerIdAndStatusIsOrderByStartDesc(anyLong(),
                        any(BookingConstant.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getAllBookingsByBookerIdAndStateNotValid() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(ValidationException.class,
                () -> service.getAllBookingsByBookerIdAndState(1L, "ALL", -1, 20));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getAllBookingsByBookerIdAndStateIllegalArgument() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.getAllBookingsByBookerIdAndState(1L, "UNKNOWN STATE", 0, 20));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getAllBookingsByOwnerIdAndState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findByItemOwnerOrderByStartDesc(anyLong(), any(PageRequest.class)))
                .thenReturn(Collections.emptyList());

        service.getAllBookingsByOwnerIdAndState(1L, "ALL", 0, 20);
        service.getAllBookingsByOwnerIdAndState(1L, "PAST", 0, 20);
        service.getAllBookingsByOwnerIdAndState(1L, "FUTURE", 0, 20);
        service.getAllBookingsByOwnerIdAndState(1L, "CURRENT", 0, 20);
        service.getAllBookingsByOwnerIdAndState(1L, "WAITING", 0, 20);
        service.getAllBookingsByOwnerIdAndState(1L, "REJECTED", 0, 20);

        verify(bookingRepository, times(1))
                .findByItemOwnerOrderByStartDesc(anyLong(), any(PageRequest.class));
        verify(bookingRepository, times(1))
                .findByItemOwnerAndEndIsBeforeOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class),
                        any(PageRequest.class));
        verify(bookingRepository, times(1))
                .findByItemOwnerAndEndIsAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class),
                        any(PageRequest.class));
        verify(bookingRepository, times(1))
                .findByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class),
                        any(PageRequest.class));
        verify(bookingRepository, times(2))
                .findByItemOwnerAndStatusIsOrderByStartDesc(anyLong(),
                        any(BookingConstant.class),
                        any(PageRequest.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getAllBookingsByOwnerIdAndStateWithoutFromAndSize() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findByItemOwnerOrderByStartDesc(anyLong()))
                .thenReturn(Collections.emptyList());

        service.getAllBookingsByOwnerIdAndState(1L, "ALL", null, null);
        service.getAllBookingsByOwnerIdAndState(1L, "PAST", null, null);
        service.getAllBookingsByOwnerIdAndState(1L, "FUTURE", null, null);
        service.getAllBookingsByOwnerIdAndState(1L, "CURRENT", null, null);
        service.getAllBookingsByOwnerIdAndState(1L, "WAITING", null, null);
        service.getAllBookingsByOwnerIdAndState(1L, "REJECTED", null, null);

        verify(bookingRepository, times(1))
                .findByItemOwnerOrderByStartDesc(anyLong());
        verify(bookingRepository, times(1))
                .findByItemOwnerAndEndIsBeforeOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class));
        verify(bookingRepository, times(1))
                .findByItemOwnerAndEndIsAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class));
        verify(bookingRepository, times(1))
                .findByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class));
        verify(bookingRepository, times(2))
                .findByItemOwnerAndStatusIsOrderByStartDesc(anyLong(),
                        any(BookingConstant.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getAllBookingsByOwnerIdAndStateNotValid() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(ValidationException.class,
                () -> service.getAllBookingsByOwnerIdAndState(1L, "ALL", -1, 20));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getAllBookingsByOwnerIdAndStateIllegalArgument() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.getAllBookingsByOwnerIdAndState(1L, "UNKNOWN STATE", 0, 20));
        verifyNoMoreInteractions(bookingRepository);
    }
}
