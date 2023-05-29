package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql(scripts = "classpath:/createDb.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ItemRepositoryIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void searchByText() {
        List<Item> items = itemRepository.searchByText("name", "name");

        assertEquals(1, items.size());
        assertEquals(1L, items.get(0).getId());
    }

    @Test
    void searchByTextPageable() {
        List<Item> items = itemRepository.searchByText("name", "name", PageRequest.of(0, 20));

        assertEquals(1, items.size());
        assertEquals(1L, items.get(0).getId());
    }

    @Test
    void findByOwner() {
        List<Item> items = itemRepository.findByOwnerOrderById(2L);

        assertEquals(1, items.size());
        assertEquals(1L, items.get(0).getId());
    }

    @Test
    void findByOwnerPageable() {
        List<Item> items = itemRepository.findByOwnerOrderById(2L, PageRequest.of(0, 20));

        assertEquals(1, items.size());
        assertEquals(1L, items.get(0).getId());
    }

    @Test
    void findByRequestId() {
        List<Item> items = itemRepository.findByRequestId(1L);

        assertEquals(1, items.size());
        assertEquals(1L, items.get(0).getId());
    }
}
