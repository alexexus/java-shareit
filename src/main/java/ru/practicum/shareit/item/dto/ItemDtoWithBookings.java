package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoItem;

@Data
@Builder
@AllArgsConstructor
public class ItemDtoWithBookings {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private BookingDtoItem lastBooking;
    private BookingDtoItem nextBooking;
}
