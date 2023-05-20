package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ItemRequestMapperTest {

    @Autowired
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
