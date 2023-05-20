package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Component
public class ItemRequestMapper {

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemRequest.getItems())
                .build();
    }

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated())
                .items(itemRequestDto.getItems())
                .build();
    }
}
