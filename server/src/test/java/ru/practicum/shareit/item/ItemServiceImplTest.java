package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingConstant;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        verify(itemRepository, times(1)).save(itemToSave);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void addItemNotFoundUser() {
        Item itemToSave = Item.builder()
                .id(1L)
                .name("name1")
                .description("description1")
                .available(true)
                .requestId(1L)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.addItem(itemToSave, 1L));
    }

    @Test
    void addItemNotFoundItemRequest() {
        Item itemToSave = Item.builder()
                .id(1L)
                .name("name1")
                .description("description1")
                .available(true)
                .requestId(1L)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.addItem(itemToSave, 1L));
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
                .start(LocalDateTime.MAX.minusDays(1))
                .end(LocalDateTime.MAX.minusHours(1))
                .status(BookingConstant.APPROVED)
                .build();
        Booking nextBooking = Booking.builder()
                .id(2L)
                .start(LocalDateTime.MAX.minusDays(1))
                .end(LocalDateTime.MAX.minusHours(1))
                .status(BookingConstant.APPROVED)
                .build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(expectedItem));
        when(commentRepository.findByItemId(anyLong())).thenReturn(Collections.emptyList());
        when(bookingRepository.findByItemId(anyLong())).thenReturn(List.of(lastBooking, nextBooking));

        Item actual = service.getItemById(1L, 1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedItem);
        verify(itemRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getItemByIdNotFoundItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.getItemById(1L, 2L));
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
        verify(itemRepository, times(1)).findById(1L);
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
        verify(itemRepository, times(1)).save(item);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void updateItemWithNullableFields() {
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
                .name(null)
                .description(null)
                .available(null)
                .owner(null)
                .requestId(1L)
                .build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemToUpdate));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item actual = service.updateItem(item, 1L, 1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(item);
        verify(itemRepository, times(1)).save(itemToUpdate);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void updateItemNotFoundItem() {
        Item itemToUpdate = Item.builder().owner(1L).build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.updateItem(itemToUpdate, 2L, 1L));
    }

    @Test
    void deleteItem() {
        doNothing().when(itemRepository).deleteById(anyLong());
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));

        service.deleteItem(1L);

        verify(itemRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void deleteItemNotFoundItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.deleteItem(1L));
    }

    @Test
    void getItemsByText() {
        when(itemRepository.searchByText(anyString(), anyString(), any(PageRequest.class)))
                .thenReturn(Collections.emptyList());

        service.getItemsByText("text", 0, 20);

        verify(itemRepository, times(1)).searchByText("text", "text",
                PageRequest.of(0 / 20, 20));
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getItemsByTextWithoutFromAndSize() {
        when(itemRepository.searchByText(anyString(), anyString())).thenReturn(Collections.emptyList());

        service.getItemsByText("text", null, null);

        verify(itemRepository, times(1)).searchByText("text", "text");
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getAllItemsByUserId() {
        Item item = Item.builder().id(1L).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findByOwnerOrderById(anyLong(), any(PageRequest.class)))
                .thenReturn(List.of(item));
        when(bookingRepository.findByItemId(anyLong())).thenReturn(Collections.emptyList());

        service.getAllItemsByUserId(1L, 0, 20);

        verify(itemRepository, times(1)).findByOwnerOrderById(1L,
                PageRequest.of(0 / 20, 20));
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getAllItemsByUserIdWithoutFromAndSize() {
        Item item = Item.builder().id(1L).build();
        Booking lastBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.MAX.minusDays(3))
                .end(LocalDateTime.MAX.minusDays(2))
                .status(BookingConstant.APPROVED)
                .build();
        Booking nextBooking = Booking.builder()
                .id(2L)
                .start(LocalDateTime.MAX.minusDays(1))
                .end(LocalDateTime.MAX.minusHours(1))
                .status(BookingConstant.APPROVED)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findByOwnerOrderById(anyLong())).thenReturn(List.of(item));
        when(bookingRepository.findByItemId(anyLong())).thenReturn(List.of(lastBooking, nextBooking));

        service.getAllItemsByUserId(1L, null, null);

        verify(itemRepository, times(1)).findByOwnerOrderById(1L);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void getAllItemsByUserIdNotFoundUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.getAllItemsByUserId(1L, 0, 20));
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
                .created(LocalDateTime.of(2000, 1, 1, 0, 0))
                .build();
        Booking booking = Booking.builder().booker(user).build();
        when(bookingRepository.findByItemIdAndBookerIdAndStatusIsAndEndIsBefore(anyLong(), anyLong(),
                any(BookingConstant.class), any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));
        when(commentRepository.save(any(Comment.class))).thenReturn(commentToSave);

        Comment actual = service.addComment(commentToSave, 1L, 1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(commentToSave);
        verify(commentRepository, times(1)).save(commentToSave);
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
                .created(LocalDateTime.of(2000, 1, 1, 0, 0))
                .build();

        assertThrows(ValidationException.class,
                () -> service.addComment(commentToSave, 1L, 1L));
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void addCommentNotValidBooker() {
        Item item = Item.builder().id(1L).build();
        User author = User.builder().id(1L).build();
        Comment commentToSave = Comment.builder()
                .id(1L)
                .text("text")
                .item(item)
                .author(author)
                .created(LocalDateTime.of(2000, 1, 1, 0, 0))
                .build();
        when(bookingRepository.findByItemIdAndBookerIdAndStatusIsAndEndIsBefore(anyLong(), anyLong(),
                any(BookingConstant.class), any(LocalDateTime.class))).thenReturn(Collections.emptyList());

        assertThrows(ValidationException.class,
                () -> service.addComment(commentToSave, 1L, 1L));
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void addCommentNotFoundUser() {
        Item item = Item.builder().id(1L).build();
        User user = User.builder().id(1L).build();
        Comment commentToSave = Comment.builder()
                .id(1L)
                .text("text")
                .item(item)
                .author(user)
                .created(LocalDateTime.of(2000, 1, 1, 0, 0))
                .build();
        Booking booking = Booking.builder().booker(user).build();
        when(bookingRepository.findByItemIdAndBookerIdAndStatusIsAndEndIsBefore(anyLong(), anyLong(),
                any(BookingConstant.class), any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.addComment(commentToSave, 1L, 1L));
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void addCommentNotFoundItem() {
        Item item = Item.builder().id(1L).build();
        User user = User.builder().id(1L).build();
        Comment commentToSave = Comment.builder()
                .id(1L)
                .text("text")
                .item(item)
                .author(user)
                .created(LocalDateTime.of(2000, 1, 1, 0, 0))
                .build();
        Booking booking = Booking.builder().booker(user).build();
        when(bookingRepository.findByItemIdAndBookerIdAndStatusIsAndEndIsBefore(anyLong(), anyLong(),
                any(BookingConstant.class), any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.addComment(commentToSave, 1L, 1L));
        verifyNoMoreInteractions(commentRepository);
    }
}
