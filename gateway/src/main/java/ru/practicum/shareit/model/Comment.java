package ru.practicum.shareit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private Long id;

    private String text;

    private Item item;

    private User author;

    private LocalDateTime created;
}
