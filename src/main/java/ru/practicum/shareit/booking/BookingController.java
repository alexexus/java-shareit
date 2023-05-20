package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.OnCreate;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    public static final String USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public Booking addBooking(@RequestHeader(USER_ID) long userId,
                              @Validated(OnCreate.class) @RequestBody BookingDto bookingDto) {
        return bookingService.addBooking(bookingMapper.toBooking(bookingDto), userId);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader(USER_ID) long userId,
                                  @PathVariable long bookingId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking updateBooking(@RequestHeader(USER_ID) long userId,
                                 @PathVariable long bookingId,
                                 @RequestParam(name = "approved") boolean approved) {
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @DeleteMapping("/{bookingId}")
    public void deleteBooking(@RequestHeader(USER_ID) long userId,
                              @PathVariable long bookingId) {
        bookingService.deleteBooking(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getAllBookingsByUserId(@RequestHeader(USER_ID) long userId,
                                                @RequestParam(name = "state",
                                                        required = false,
                                                        defaultValue = "ALL") String state,
                                                @RequestParam(name = "from",
                                                        required = false) Integer from,
                                                @RequestParam(name = "size",
                                                        required = false) Integer size) {
        return bookingService.getAllBookingsByBookerIdAndState(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<Booking> getAllBookingsByState(@RequestHeader(USER_ID) long userId,
                                               @RequestParam(name = "state",
                                                       required = false,
                                                       defaultValue = "ALL") String state,
                                               @RequestParam(name = "from",
                                                       required = false) Integer from,
                                               @RequestParam(name = "size",
                                                       required = false) Integer size) {
        return bookingService.getAllBookingsByOwnerIdAndState(userId, state, from, size);
    }
}
