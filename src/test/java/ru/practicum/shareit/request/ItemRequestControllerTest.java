package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @InjectMocks
    private ItemRequestController controller;
    @Mock
    private ItemRequestService service;
    @Mock
    private ItemRequestMapper mapper;

    @Test
    void addItemRequest() {
        User user = User.builder().id(1L).build();
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("description")
                .requestor(user)
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();
        when(mapper.toItemRequest(any(ItemRequestDto.class))).thenReturn(itemRequest);
        when(mapper.toItemRequestDto(any(ItemRequest.class))).thenReturn(itemRequestDto);
        when(service.addItemRequest(any(ItemRequest.class), anyLong())).thenReturn(itemRequest);

        ItemRequestDto actual = controller.addItemRequest(1L, itemRequestDto);

        assertEquals(actual, itemRequestDto);
        verify(service, times(1)).addItemRequest(any(ItemRequest.class), anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getItemRequestById() {
        User user = User.builder().id(1L).build();
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("description")
                .requestor(user)
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();
        when(mapper.toItemRequestDto(any(ItemRequest.class))).thenReturn(itemRequestDto);
        when(service.getItemRequestById(anyLong(), anyLong())).thenReturn(itemRequest);

        ItemRequestDto actual = controller.getItemRequestById(1L, 1L);

        assertEquals(actual, itemRequestDto);
        verify(service, times(1)).getItemRequestById(anyLong(), anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllItemRequestsPageable() {
        when(service.getAllItemRequestsPageable(anyLong(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        List<ItemRequestDto> actual = controller.getAllItemRequestsPageable(1L, 0, 20);

        assertEquals(0, actual.size());
        verify(service, times(1)).getAllItemRequestsPageable(anyLong(), anyInt(), anyInt());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllItemRequests() {
        when(service.getAllItemRequests(anyLong())).thenReturn(Collections.emptyList());

        List<ItemRequestDto> actual = controller.getAllItemRequests(1L);

        assertEquals(0, actual.size());
        verify(service, times(1)).getAllItemRequests(anyLong());
        verifyNoMoreInteractions(service);
    }
}
