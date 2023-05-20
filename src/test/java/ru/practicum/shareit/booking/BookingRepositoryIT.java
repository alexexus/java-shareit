package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingRepositoryIT {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    private void setUp() {
        User booker = userRepository.save(User.builder()
                .id(1L)
                .name("1name")
                .email("1@mail.ru")
                .build());
        User owner = userRepository.save(User.builder()
                .id(2L)
                .name("2name")
                .email("2@mail.ru")
                .build());
        Item item = itemRepository.save(Item.builder()
                .id(1L)
                .name("1name")
                .description("1description")
                .available(true)
                .owner(owner.getId())
                .build());
        bookingRepository.save(Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingConstant.WAITING)
                .build());
    }

    @Test
    @Order(1)
    void findByBookerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(1L);

        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    @Order(2)
    void findByBookerIdOrderByStartDescPageable() {
        List<Booking> bookings = bookingRepository
                .findByBookerIdOrderByStartDesc(3L, PageRequest.of(0, 20));

        assertEquals(1, bookings.size());
        assertEquals(2L, bookings.get(0).getId());
    }

    @Test
    @Order(3)
    void findByItemOwnerOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByItemOwnerOrderByStartDesc(6L);

        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getId());
    }

    @Test
    @Order(4)
    void findByItemOwnerOrderByStartDescPageable() {
        List<Booking> bookings = bookingRepository
                .findByItemOwnerOrderByStartDesc(8L, PageRequest.of(0, 20));

        assertEquals(1, bookings.size());
        assertEquals(4L, bookings.get(0).getId());
    }

    @Test
    @Order(5)
    @Sql(scripts = "classpath:/clearDb.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByItemId() {
        List<Booking> bookings = bookingRepository.findByItemId(5L);

        assertEquals(1, bookings.size());
        assertEquals(5L, bookings.get(0).getId());
    }

    @AfterEach
    private void cleanDb() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        bookingRepository.deleteAll();
    }
}
