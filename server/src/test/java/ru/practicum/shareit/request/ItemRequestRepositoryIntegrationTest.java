package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql(scripts = "classpath:/createDb.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ItemRequestRepositoryIntegrationTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findByRequestorIdNotOrderByCreatedDesc() {
        List<ItemRequest> itemRequests = itemRequestRepository
                .findByRequestorIdNotOrderByCreatedDesc(1L, PageRequest.of(0, 20));

        assertEquals(1, itemRequests.size());
        assertEquals(2L, itemRequests.get(0).getRequestor().getId());
    }

    @Test
    void findByRequestorId() {
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorId(2L);

        assertEquals(1, itemRequests.size());
        assertEquals(2L, itemRequests.get(0).getRequestor().getId());
    }
}
