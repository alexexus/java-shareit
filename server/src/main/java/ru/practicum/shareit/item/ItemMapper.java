package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ItemMapper {

    private BookingMapper bookingMapper;
    private CommentMapper commentMapper;

    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .requestId(item.getRequestId())
                .build();
    }

    public Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(itemDto.getOwner())
                .requestId(itemDto.getRequestId())
                .build();
    }

    public ItemDtoWithBookings toItemDtoWithBookings(Item item) {
        return ItemDtoWithBookings.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .lastBooking(bookingMapper.toBookingDtoItem(item.getLastBooking()))
                .nextBooking(bookingMapper.toBookingDtoItem(item.getNextBooking()))
                .build();
    }

    public ItemDtoWithComments toItemDtoWithComments(Item item) {
        return ItemDtoWithComments.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .lastBooking(bookingMapper.toBookingDtoItem(item.getLastBooking()))
                .nextBooking(bookingMapper.toBookingDtoItem(item.getNextBooking()))
                .comments(item.getComments().stream().map(commentMapper::toCommentDto).collect(Collectors.toList()))
                .build();
    }
}
