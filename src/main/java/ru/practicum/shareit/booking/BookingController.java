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

    private final BookingServiceImpl bookingServiceImpl;

    @PostMapping
    public Booking addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Validated(OnCreate.class) @RequestBody BookingDto bookingDto) {
        return bookingServiceImpl.addBooking(BookingMapper.toBooking(bookingDto), userId);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long bookingId) {
        return bookingServiceImpl.getBookingById(bookingId, userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking updateBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable long bookingId,
                                 @RequestParam(name = "approved") boolean approved) {
        return bookingServiceImpl.updateBooking(userId, bookingId, approved);
    }

    @DeleteMapping("/{bookingId}")
    public void deleteBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long bookingId) {
        bookingServiceImpl.deleteBooking(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getAllBookingsByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(name = "state",
                                                        required = false,
                                                        defaultValue = "ALL") String state) {
        try {
            StateConstant constant = StateConstant.valueOf(state);
            switch (constant) {
                case ALL:
                    return bookingServiceImpl.getAllBookingsByBookerId(userId);
                case PAST:
                    return bookingServiceImpl.getAllBookingsByBookerIdAndPast(userId);
                case FUTURE:
                    return bookingServiceImpl.getAllBookingsByBookerIdAndFuture(userId);
                case WAITING:
                case REJECTED:
                case CURRENT:
                    return bookingServiceImpl.getAllBookingsByBookerIdAndState(userId, constant);
            }
            return bookingServiceImpl.getAllBookingsByBookerId(userId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    @GetMapping("/owner")
    public List<Booking> getAllBookingsByState(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(name = "state",
                                                       required = false,
                                                       defaultValue = "ALL") String state) {
        try {
            StateConstant constant = StateConstant.valueOf(state);
            switch (constant) {
                case ALL:
                    return bookingServiceImpl.getAllBookingsByOwnerId(userId);
                case PAST:
                    return bookingServiceImpl.getAllBookingsByOwnerIdAndPast(userId);
                case FUTURE:
                    return bookingServiceImpl.getAllBookingsByOwnerIdAndFuture(userId);
                case WAITING:
                case REJECTED:
                case CURRENT:
                    return bookingServiceImpl.getAllBookingsByOwnerIdAndState(userId, constant);
            }
            return bookingServiceImpl.getAllBookingsByOwnerId(userId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }
}
