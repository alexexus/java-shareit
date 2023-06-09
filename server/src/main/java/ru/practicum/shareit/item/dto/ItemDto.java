package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.OnCreate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    private Long owner;

    private Long requestId;
}
