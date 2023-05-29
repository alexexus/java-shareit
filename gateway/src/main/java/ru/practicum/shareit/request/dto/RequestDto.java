package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.OnCreate;
import ru.practicum.shareit.model.Item;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RequestDto {

    private Long id;

    @NotBlank(groups = {OnCreate.class})
    private String description;

    private LocalDateTime created;

    private List<Item> items;
}