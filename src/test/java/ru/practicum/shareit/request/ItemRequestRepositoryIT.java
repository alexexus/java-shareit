package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemRequestRepositoryIT {

    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    private void setUp() {
        User user = userRepository.save(User.builder()
                .id(1L)
                .name("1name")
                .email("1@mail.ru")
                .build());
        userRepository.save(User.builder()
                .id(2L)
                .name("2name")
                .email("2@mail.ru")
                .build());
        itemRequestRepository.save(ItemRequest.builder()
                .id(1L)
                .description("description")
                .requestor(User.builder().id(user.getId()).build())
                .created(LocalDateTime.now())
                .build());
    }

    @Test
    @Order(1)
    void findByRequestorIdNotOrderByCreatedDesc() {
        List<ItemRequest> itemRequests = itemRequestRepository
                .findByRequestorIdNotOrderByCreatedDesc(2L, PageRequest.of(0, 20));

        assertEquals(1, itemRequests.size());
        assertEquals(1L, itemRequests.get(0).getRequestor().getId());
    }

    @Test
    @Order(2)
    @Sql(scripts = "classpath:/clearDb.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByRequestorId() {
        List<ItemRequest> itemRequests = itemRequestRepository
                .findByRequestorId(3L);

        assertEquals(1, itemRequests.size());
        assertEquals(3L, itemRequests.get(0).getRequestor().getId());
    }

    @AfterEach
    private void cleanDb() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }
}
