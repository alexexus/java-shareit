package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingConstant;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public Item addItem(Item item, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        item.setOwner(userId);
        if (item.getRequestId() != null) {
            itemRequestRepository.findById(item.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Item request not found"));
        }
        return itemRepository.save(item);
    }

    @Override
    public Item getItemById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        item.setComments(commentRepository.findByItemId(itemId).stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList()));
        List<Booking> bookings = bookingRepository.findByItemId(itemId);
        if (bookings.isEmpty() || item.getOwner() != userId) {
            return item;
        }
        item.setLastBooking(bookingMapper.toBookingDtoItem(getLastBooking(bookings)));
        item.setNextBooking(bookingMapper.toBookingDtoItem(getNextBooking(bookings)));
        return item;
    }

    @Override
    public Item updateItem(Item item, long userId, long itemId) {
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        if (oldItem.getOwner() != userId) {
            throw new NotFoundException("User not exist");
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getOwner() != null && item.getOwner() > 0) {
            oldItem.setOwner(item.getOwner());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        return itemRepository.save(oldItem);
    }

    @Override
    public void deleteItem(long itemId) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<Item> getItemsByText(String text, Integer from, Integer size) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        if (from == null || size == null) {
            return itemRepository
                    .findByNameContainingIgnoreCaseAndAvailableIsTrueOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                            text, text);
        }
        if (size < 1 || from < 0) {
            throw new ValidationException("Size cannot be less than 1 and from cannot be less than 0");
        }
        return itemRepository
                .findByNameContainingIgnoreCaseAndAvailableIsTrueOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                        text, text, PageRequest.of(from / size, size));
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (from == null || size == null) {
            List<Item> items = itemRepository.findByOwner(userId);
            return getItems(items);
        }
        if (size < 1 || from < 0) {
            throw new ValidationException("Size cannot be less than 1 and from cannot be less than 0");
        }
        List<Item> items = itemRepository.findByOwner(userId, PageRequest.of(from / size, size));
        return getItems(items);
    }

    @Override
    public Comment addComment(Comment comment, long userId, long itemId) {
        if (comment.getText().isBlank()) {
            throw new ValidationException("Text is empty");
        }
        List<Long> bookings = bookingRepository.findByItemIdAndEndIsBefore(itemId, LocalDateTime.now()).stream()
                .map(booking -> booking.getBooker().getId())
                .collect(Collectors.toList());
        if (!bookings.contains(userId)) {
            throw new ValidationException("User did not rent this item");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    private Booking getNextBooking(List<Booking> bookings) {
        return bookings.stream()
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()) &&
                        b.getStatus().equals(BookingConstant.APPROVED))
                .min(Comparator.comparing(Booking::getStart))
                .orElse(null);
    }

    private Booking getLastBooking(List<Booking> bookings) {
        return bookings.stream()
                .filter(b -> b.getStart().isBefore(LocalDateTime.now()) &&
                        b.getStatus().equals(BookingConstant.APPROVED))
                .max(Comparator.comparing(Booking::getStart))
                .orElse(null);
    }

    private List<Item> getItems(List<Item> items) {
        for (Item i : items) {
            List<Booking> bookings = bookingRepository.findByItemId(i.getId());
            if (bookings.isEmpty()) {
                continue;
            }
            i.setLastBooking(bookingMapper.toBookingDtoItem(getLastBooking(bookings)));
            i.setNextBooking(bookingMapper.toBookingDtoItem(getNextBooking(bookings)));
        }
        return items;
    }
}
