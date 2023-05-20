package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestServiceImpl service;

    @Test
    void addItemRequest() {
        User user = User.builder().id(1L).build();
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("description")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequest actual = service.addItemRequest(itemRequest, 1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(itemRequest);
        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));
        verifyNoMoreInteractions(itemRequestRepository);
    }

    @Test
    void getItemRequestById() {
        User user = User.builder().id(1L).build();
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("description")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findByRequestId(anyLong())).thenReturn(Collections.emptyList());

        ItemRequest actual = service.getItemRequestById(1L, 1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(itemRequest);
        verify(itemRequestRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(itemRequestRepository);
    }

    @Test
    void getAllItemRequestsPageable() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(anyLong(), any(PageRequest.class)))
                .thenReturn(Collections.emptyList());

        service.getAllItemRequestsPageable(1L, 0, 20);

        verify(itemRequestRepository, times(1))
                .findByRequestorIdNotOrderByCreatedDesc(anyLong(), any(PageRequest.class));
        verifyNoMoreInteractions(itemRequestRepository);
    }

    @Test
    void getAllItemRequestsPageableWithoutFromAndSize() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findByRequestorId(anyLong())).thenReturn(Collections.emptyList());

        service.getAllItemRequestsPageable(1L, null, null);

        verify(itemRequestRepository, times(1)).findByRequestorId(anyLong());
        verifyNoMoreInteractions(itemRequestRepository);
    }

    @Test
    void getAllItemRequestsPageableNotValid() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(ValidationException.class,
                () -> service.getAllItemRequestsPageable(1L, -1, 20));
    }

    @Test
    void getAllItemRequests() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findByRequestorId(anyLong())).thenReturn(Collections.emptyList());

        service.getAllItemRequests(1L);

        verify(itemRequestRepository, times(1)).findByRequestorId(anyLong());
        verifyNoMoreInteractions(itemRequestRepository);
    }
}
