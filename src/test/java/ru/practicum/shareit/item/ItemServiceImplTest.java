package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingConstant;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl service;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private CommentMapper commentMapper;

    @Test
    void addItem() {
        Item itemToSave = Item.builder()
                .id(1L)
                .name("name1")
                .description("description1")
                .available(true)
                .requestId(1L)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(new ItemRequest()));
        when(itemRepository.save(any(Item.class))).thenReturn(itemToSave);

        Item actual = service.addItem(itemToSave, 1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(itemToSave);
        verify(itemRepository, times(1)).save(any(Item.class));
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getItemById() {
        Item expectedItem = Item.builder()
                .id(1L)
                .name("name1")
                .description("description1")
                .available(true)
                .owner(1L)
                .requestId(1L)
                .build();
        Booking lastBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().minusHours(1))
                .status(BookingConstant.APPROVED)
                .build();
        Booking nextBooking = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .status(BookingConstant.APPROVED)
                .build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(expectedItem));
        when(commentRepository.findByItemId(anyLong())).thenReturn(Collections.emptyList());
        when(bookingRepository.findByItemId(anyLong())).thenReturn(List.of(lastBooking, nextBooking));

        Item actual = service.getItemById(1L, 1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedItem);
        verify(itemRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getItemByIdWithoutBookings() {
        Item expectedItem = Item.builder()
                .id(1L)
                .name("name1")
                .description("description1")
                .available(true)
                .owner(1L)
                .requestId(1L)
                .build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(expectedItem));
        when(commentRepository.findByItemId(anyLong())).thenReturn(Collections.emptyList());
        when(bookingRepository.findByItemId(anyLong())).thenReturn(Collections.emptyList());

        Item actual = service.getItemById(1L, 1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedItem);
        verify(itemRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void updateItem() {
        Item itemToUpdate = Item.builder()
                .id(1L)
                .name("name1")
                .description("description1")
                .available(true)
                .owner(1L)
                .requestId(1L)
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("name1update")
                .description("description1")
                .available(true)
                .owner(1L)
                .requestId(1L)
                .build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemToUpdate));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item actual = service.updateItem(item, 1L, 1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(item);
        verify(itemRepository, times(1)).save(any(Item.class));
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void updateItemNotFound() {
        Item itemToUpdate = Item.builder().owner(1L).build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemToUpdate));

        Assertions.assertThrows(NotFoundException.class, () -> service.updateItem(itemToUpdate, 2L, 1L));
    }

    @Test
    void deleteItem() {
        doNothing().when(itemRepository).deleteById(anyLong());
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));

        service.deleteItem(1L);

        verify(itemRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getItemsByText() {
        when(itemRepository
                .findByNameContainingIgnoreCaseAndAvailableIsTrueOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                        anyString(), anyString(), any(PageRequest.class))).thenReturn(Collections.emptyList());

        service.getItemsByText("text", 0, 20);

        verify(itemRepository, times(1))
                .findByNameContainingIgnoreCaseAndAvailableIsTrueOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                        anyString(), anyString(), any(PageRequest.class));
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getItemsByTextNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> service.getItemsByText("text", -1, 20));
    }

    @Test
    void getItemsByTextWithoutFromAndSize() {
        when(itemRepository
                .findByNameContainingIgnoreCaseAndAvailableIsTrueOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                        anyString(), anyString())).thenReturn(Collections.emptyList());

        service.getItemsByText("text", null, null);

        verify(itemRepository, times(1))
                .findByNameContainingIgnoreCaseAndAvailableIsTrueOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                        anyString(), anyString());
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getItemsByTextWithBlankText() {
        List<Item> items = service.getItemsByText(" ", null, null);

        assertThat(items).usingRecursiveComparison().isEqualTo(Collections.emptyList());
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getAllItemsByUserId() {
        Item item = Item.builder().id(1L).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findByOwner(anyLong(), any(PageRequest.class)))
                .thenReturn(List.of(item));
        when(bookingRepository.findByItemId(anyLong())).thenReturn(Collections.emptyList());

        service.getAllItemsByUserId(1L, 0, 20);

        verify(itemRepository, times(1)).findByOwner(anyLong(), any(PageRequest.class));
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getAllItemsByUserIdWithoutFromAndSize() {
        Item item = Item.builder().id(1L).build();
        Booking lastBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().minusHours(1))
                .status(BookingConstant.APPROVED)
                .build();
        Booking nextBooking = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .status(BookingConstant.APPROVED)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findByOwner(anyLong())).thenReturn(List.of(item));
        when(bookingRepository.findByItemId(anyLong())).thenReturn(List.of(lastBooking, nextBooking));

        service.getAllItemsByUserId(1L, null, null);

        verify(itemRepository, times(1)).findByOwner(anyLong());
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getAllItemsByUserIdNotValid() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(ValidationException.class,
                () -> service.getAllItemsByUserId(1L, -1, 20));
    }

    @Test
    void addComment() {
        Item item = Item.builder().id(1L).build();
        User user = User.builder().id(1L).build();
        Comment commentToSave = Comment.builder()
                .id(1L)
                .text("text")
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build();
        Booking booking = Booking.builder().booker(user).build();
        when(bookingRepository.findByItemIdAndEndIsBefore(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));
        when(commentRepository.save(any(Comment.class))).thenReturn(commentToSave);

        Comment actual = service.addComment(commentToSave, 1L, 1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(commentToSave);
        verify(commentRepository, times(1)).save(any(Comment.class));
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void addCommentNotValidComment() {
        Item item = Item.builder().id(1L).build();
        User user = User.builder().id(1L).build();
        Comment commentToSave = Comment.builder()
                .id(1L)
                .text(" ")
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build();

        Assertions.assertThrows(ValidationException.class,
                () -> service.addComment(commentToSave, 1L, 1L));
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void addCommentNotValidBooker() {
        Item item = Item.builder().id(1L).build();
        User author = User.builder().id(1L).build();
        User user = User.builder().id(2L).build();
        Booking booking = Booking.builder().booker(user).build();
        Comment commentToSave = Comment.builder()
                .id(1L)
                .text("text")
                .item(item)
                .author(author)
                .created(LocalDateTime.now())
                .build();
        when(bookingRepository.findByItemIdAndEndIsBefore(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));

        Assertions.assertThrows(ValidationException.class,
                () -> service.addComment(commentToSave, 1L, 1L));
        verifyNoMoreInteractions(commentRepository);
    }
}
