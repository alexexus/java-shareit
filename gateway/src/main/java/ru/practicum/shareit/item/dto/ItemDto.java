package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.OnCreate;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(groups = {OnCreate.class})
    private String name;

    @NotBlank(groups = {OnCreate.class})
    private String description;

    @NotNull(groups = {OnCreate.class})
    private Boolean available;

    private Long requestId;

    private BookingRequestDto lastBooking;

    private BookingRequestDto nextBooking;

    private List<CommentDto> comments;
}
