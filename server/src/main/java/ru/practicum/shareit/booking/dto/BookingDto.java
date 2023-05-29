package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.OnCreate;
import ru.practicum.shareit.booking.BookingConstant;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingDto {

    private Long id;

    @FutureOrPresent(groups = {OnCreate.class})
    @NotNull(groups = {OnCreate.class})
    private LocalDateTime start;

    @Future(groups = {OnCreate.class})
    @NotNull(groups = {OnCreate.class})
    private LocalDateTime end;

    private BookingConstant status;

    private User booker;

    private Long itemId;
}
