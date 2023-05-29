package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.OnCreate;
import ru.practicum.shareit.model.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {

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
