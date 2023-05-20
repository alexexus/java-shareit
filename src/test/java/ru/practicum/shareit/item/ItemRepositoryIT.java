package ru.practicum.shareit.item;

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
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemRepositoryIT {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @BeforeEach
    private void setUp() {
        User owner = userRepository.save(User.builder()
                .id(1L)
                .name("1name")
                .email("1mail@mail.ru")
                .build());
        User requestor = userRepository.save(User.builder()
                .id(2L)
                .name("2name")
                .email("2mail@mail.ru")
                .build());
        ItemRequest itemRequest = itemRequestRepository.save(ItemRequest.builder()
                .id(1L)
                .description("description")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build());
        itemRepository.save(Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(owner.getId())
                .requestId(itemRequest.getId())
                .build());
    }

    @Test
    @Order(1)
    void findByNameContainingIgnoreCaseAndAvailableIsTrueOrDescriptionContainingIgnoreCaseAndAvailableIsTrue() {
        List<Item> items = itemRepository
                .findByNameContainingIgnoreCaseAndAvailableIsTrueOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                        "name", "name"
                );

        assertEquals(1, items.size());
        assertEquals(1L, items.get(0).getId());
    }

    @Test
    @Order(2)
    void findByNameContainingIgnoreCaseAndAvailableIsTrueOrDescriptionContainingIgnoreCaseAndAvailableIsTruePageable() {
        List<Item> items = itemRepository
                .findByNameContainingIgnoreCaseAndAvailableIsTrueOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                        "name", "name", PageRequest.of(0, 20)
                );

        assertEquals(1, items.size());
        assertEquals(2L, items.get(0).getId());
    }

    @Test
    @Order(3)
    void findByOwner() {
        List<Item> items = itemRepository.findByOwner(5L);

        assertEquals(1, items.size());
        assertEquals(3L, items.get(0).getId());
    }

    @Test
    @Order(4)
    void findByOwnerPageable() {
        List<Item> items = itemRepository.findByOwner(7L, PageRequest.of(0, 20));

        assertEquals(1, items.size());
        assertEquals(4L, items.get(0).getId());
    }

    @Test
    @Order(5)
    @Sql(scripts = "classpath:/clearDb.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByRequestId() {
        List<Item> items = itemRepository.findByRequestId(5L);

        assertEquals(1, items.size());
        assertEquals(5L, items.get(0).getId());
    }

    @AfterEach
    private void cleanDb() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }
}
