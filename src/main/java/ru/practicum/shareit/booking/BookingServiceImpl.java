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

    public List<Booking> getAllBookingsByBookerId(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return bookingRepository.findAllByBooker_IdOrderByStartDesc(userId);
    }

    public List<Booking> getAllBookingsByBookerIdAndState(long userId, StateConstant state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (state == StateConstant.CURRENT) {
            return bookingRepository.findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                    userId,
                    LocalDateTime.now(),
                    LocalDateTime.now());
        }
        return bookingRepository.findAllByBookerIdAndState(userId, BookingConstant.valueOf(state.toString()));
    }

    public List<Booking> getAllBookingsByBookerIdAndPast(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return bookingRepository.findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now());
    }

    public List<Booking> getAllBookingsByBookerIdAndFuture(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return bookingRepository.findAllByBooker_IdAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now());
    }

    public List<Booking> getAllBookingsByOwnerId(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return bookingRepository.findAllByItem_OwnerOrderByStartDesc(userId);
    }

    public List<Booking> getAllBookingsByOwnerIdAndState(long userId, StateConstant state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (state == StateConstant.CURRENT) {
            return bookingRepository.findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                    userId,
                    LocalDateTime.now(),
                    LocalDateTime.now());
        }
        return bookingRepository.findAllByItemOwnerAndState(userId, BookingConstant.valueOf(state.toString()));
    }

    public List<Booking> getAllBookingsByOwnerIdAndPast(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return bookingRepository.findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now());
    }

    public List<Booking> getAllBookingsByOwnerIdAndFuture(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return bookingRepository.findAllByItem_OwnerAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now());
    }
}
