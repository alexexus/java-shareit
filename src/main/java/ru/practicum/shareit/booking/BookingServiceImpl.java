package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public Booking addBooking(Booking booking, long userId) {
        booking.setBooker(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found")));
        booking.setItem(itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new NotFoundException("Item not found")));
        booking.setStatus(BookingConstant.WAITING);
        if (!booking.getItem().getAvailable()) {
            throw new ValidationException("Item not available");
        }
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getEnd().isEqual(booking.getStart())) {
            throw new ValidationException("Wrong time");
        }
        if (booking.getItem().getOwner() == userId) {
            throw new NotFoundException("The owner cannot book his item");
        }
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(long bookingId, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner() != userId) {
            throw new NotFoundException("User not owner or booker");
        }
        return booking;
    }

    public Booking updateBooking(long userId, long bookingId, boolean approved) {
        Booking booking = getBookingById(bookingId, userId);
        if (booking.getItem().getOwner() != userId) {
            throw new NotFoundException("User is not owner");
        }
        if (booking.getStatus().equals(BookingConstant.APPROVED) && approved) {
            throw new ValidationException("Status is already approved");
        }
        if (approved) {
            booking.setStatus(BookingConstant.APPROVED);
        } else {
            booking.setStatus(BookingConstant.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    public void deleteBooking(long bookingId, long userId) {
        getBookingById(bookingId, userId);
        bookingRepository.deleteById(bookingId);
    }

    public List<Booking> getAllBookingsByBookerIdAndState(long userId, String state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        try {
            StateConstant constant = StateConstant.valueOf(state);
            switch (constant) {
                case ALL:
                    return bookingRepository.findByBookerIdOrderByStartDesc(userId);
                case PAST:
                    return bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId,
                            LocalDateTime.now());
                case FUTURE:
                    return bookingRepository.findByBookerIdAndEndIsAfterOrderByStartDesc(userId,
                            LocalDateTime.now());
                case CURRENT:
                    return bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                            LocalDateTime.now(),
                            LocalDateTime.now());
                case WAITING:
                case REJECTED:
                    return bookingRepository.findByBookerIdAndState(userId,
                            BookingConstant.valueOf(constant.toString()));
            }
            return bookingRepository.findByBookerIdOrderByStartDesc(userId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    public List<Booking> getAllBookingsByOwnerIdAndState(long userId, String state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        try {
            StateConstant constant = StateConstant.valueOf(state);
            switch (constant) {
                case ALL:
                    return bookingRepository.findByItemOwnerOrderByStartDesc(userId);
                case PAST:
                    return bookingRepository.findByItemOwnerAndEndIsBeforeOrderByStartDesc(userId,
                            LocalDateTime.now());
                case FUTURE:
                    return bookingRepository.findByItemOwnerAndEndIsAfterOrderByStartDesc(userId,
                            LocalDateTime.now());
                case CURRENT:
                    return bookingRepository.findByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                            userId,
                            LocalDateTime.now(),
                            LocalDateTime.now());
                case WAITING:
                case REJECTED:
                    return bookingRepository.findByItemOwnerAndState(userId,
                            BookingConstant.valueOf(constant.toString()));
            }
            return bookingRepository.findByItemOwnerOrderByStartDesc(userId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }
}
