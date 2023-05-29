package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    public static final String USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader(USER_ID) Long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL", required = false)
                                              String state,
                                              @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0")
                                              Integer from,
                                              @Positive @RequestParam(name = "size", required = false, defaultValue = "10")
                                              Integer size) {
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        return bookingClient.getBookings(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(@RequestHeader(USER_ID) Long userId,
                                                     @RequestParam(name = "state", defaultValue = "ALL", required = false)
                                                     String state,
                                                     @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0")
                                                     Integer from,
                                                     @Positive @RequestParam(name = "size", required = false, defaultValue = "10")
                                                     Integer size) {
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        return bookingClient.getBookingsByOwner(userId, bookingState, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader(USER_ID) Long userId,
                                           @RequestBody @Valid BookingRequestDto requestDto) {
        return bookingClient.bookItem(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(USER_ID) Long userId,
                                                @PathVariable Long bookingId,
                                                @RequestParam(name = "approved") boolean approved) {
        return bookingClient.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(USER_ID) Long userId,
                                             @PathVariable Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Object> deleteBooking(@RequestHeader(USER_ID) Long userId,
                                                @PathVariable Long bookingId) {
        return bookingClient.deleteBooking(userId, bookingId);
    }
}
