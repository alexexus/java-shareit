package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @InjectMocks
    private CommentMapper commentMapper;

    @Test
    void toCommentDto() {
        User author = User.builder()
                .id(1L)
                .name("name")
                .email("mail@mail.ru")
                .build();
        Item item = Item.builder()
                .id(1L)
                .comments(Collections.emptyList())
                .build();
        Comment comment = Comment.builder()
                .id(1L)
                .text("text")
                .item(item)
                .author(author)
                .created(LocalDateTime.of(2000, 1, 1, 0, 0))
                .build();
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("text")
                .authorName("name")
                .created(LocalDateTime.of(2000, 1, 1, 0, 0))
                .build();

        assertEquals(commentDto, commentMapper.toCommentDto(comment));
    }
}
