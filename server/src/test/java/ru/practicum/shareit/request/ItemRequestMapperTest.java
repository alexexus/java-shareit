package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemRequestMapperTest {

    @InjectMocks
    private ItemRequestMapper itemRequestMapper;

    @Test
    void toItemRequestDto() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.of(2000, 1, 1, 0, 0))
                .items(List.of(Item.builder().id(1L).build()))
                .build();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.of(2000, 1, 1, 0, 0))
                .items(List.of(Item.builder().id(1L).build()))
                .build();

        assertEquals(itemRequestDto, itemRequestMapper.toItemRequestDto(itemRequest));
    }

    @Test
    void toItemRequest() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.of(2000, 1, 1, 0, 0))
                .items(List.of(Item.builder().id(1L).build()))
                .build();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.of(2000, 1, 1, 0, 0))
                .items(List.of(Item.builder().id(1L).build()))
                .build();

        assertEquals(itemRequest, itemRequestMapper.toItemRequest(itemRequestDto));
    }
}
