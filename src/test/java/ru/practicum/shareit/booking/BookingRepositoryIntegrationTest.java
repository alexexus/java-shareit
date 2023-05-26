package ru.practicum.shareit.booking;

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
class BookingRepositoryIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void findByBookerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(1L);

        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findByBookerIdOrderByStartDescPageable() {
        List<Booking> bookings = bookingRepository
                .findByBookerIdOrderByStartDesc(1L, PageRequest.of(0, 20));

        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findByItemOwnerOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByItemOwnerOrderByStartDesc(2L);

        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findByItemOwnerOrderByStartDescPageable() {
        List<Booking> bookings = bookingRepository
                .findByItemOwnerOrderByStartDesc(2L, PageRequest.of(0, 20));

        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findByItemId() {
        List<Booking> bookings = bookingRepository.findByItemId(1L);

        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }
}
