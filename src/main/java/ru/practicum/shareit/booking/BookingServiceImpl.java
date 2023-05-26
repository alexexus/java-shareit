package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public List<Booking> getAllBookingsByBookerIdAndState(long userId, String state, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
        if (from != null && size != null) {
            if (size < 1 || from < 0) {
                throw new ValidationException("Size cannot be less than 1 and from cannot be less than 0");
            }
            switch (bookingState) {
                case ALL:
                    return bookingRepository.findByBookerIdOrderByStartDesc(userId, PageRequest.of(from / size, size));
                case PAST:
                    return bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId,
                            LocalDateTime.now(),
                            PageRequest.of(from / size, size));
                case FUTURE:
                    return bookingRepository.findByBookerIdAndEndIsAfterOrderByStartDesc(userId,
                            LocalDateTime.now(),
                            PageRequest.of(from / size, size));
                case CURRENT:
                    return bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            PageRequest.of(from / size, size));
                case WAITING:
                case REJECTED:
                    return bookingRepository.findByBookerIdAndStatusIsOrderByStartDesc(userId,
                            BookingConstant.valueOf(bookingState.toString()),
                            PageRequest.of(from / size, size));
            }
            return bookingRepository.findByBookerIdOrderByStartDesc(userId, PageRequest.of(from / size, size));
        }
        switch (bookingState) {
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
                return bookingRepository.findByBookerIdAndStatusIsOrderByStartDesc(userId,
                        BookingConstant.valueOf(bookingState.toString()));
        }
        return bookingRepository.findByBookerIdOrderByStartDesc(userId);
    }

    @Override
    public List<Booking> getAllBookingsByOwnerIdAndState(long userId, String state, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
        if (from != null && size != null) {
            if (size < 1 || from < 0) {
                throw new ValidationException("Size cannot be less than 1 and from cannot be less than 0");
            }
            switch (bookingState) {
                case ALL:
                    return bookingRepository.findByItemOwnerOrderByStartDesc(userId,
                            PageRequest.of(from / size, size));
                case PAST:
                    return bookingRepository.findByItemOwnerAndEndIsBeforeOrderByStartDesc(userId,
                            LocalDateTime.now(),
                            PageRequest.of(from / size, size));
                case FUTURE:
                    return bookingRepository.findByItemOwnerAndEndIsAfterOrderByStartDesc(userId,
                            LocalDateTime.now(),
                            PageRequest.of(from / size, size));
                case CURRENT:
                    return bookingRepository.findByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                            userId,
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            PageRequest.of(from / size, size));
                case WAITING:
                case REJECTED:
                    return bookingRepository.findByItemOwnerAndStatusIsOrderByStartDesc(userId,
                            BookingConstant.valueOf(bookingState.toString()), PageRequest.of(from / size, size));
            }
            return bookingRepository.findByItemOwnerOrderByStartDesc(userId, PageRequest.of(from / size, size));
        }
        switch (bookingState) {
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
                return bookingRepository.findByItemOwnerAndStatusIsOrderByStartDesc(userId,
                        BookingConstant.valueOf(bookingState.toString()));
        }
        return bookingRepository.findByItemOwnerOrderByStartDesc(userId);
    }
}
