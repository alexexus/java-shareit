package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingConstant;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
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

    public Item addItem(Item item, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        item.setOwner(userId);
        return itemRepository.save(item);
    }

    public ItemDtoWithComments getItemById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        ItemDtoWithComments itemDtoWithComments = ItemMapper.toItemDtoWithComments(item);
        itemDtoWithComments.setComments(commentRepository.findAllByItem_Id(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));
        List<Booking> bookings = bookingRepository.findAllByItem_Id(itemId);
        if (bookings.isEmpty() || itemDtoWithComments.getOwner() != userId) {
            return itemDtoWithComments;
        }
        itemDtoWithComments.setLastBooking(BookingMapper.toBookingDtoItem(getLastBooking(bookings)));
        itemDtoWithComments.setNextBooking(BookingMapper.toBookingDtoItem(getNextBooking(bookings)));
        return itemDtoWithComments;
    }

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

    public void deleteItem(long itemId) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        itemRepository.deleteById(itemId);
    }

    public List<Item> getItemsByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findAllByText(text);
    }

    public List<ItemDtoWithBookings> getAllItemsByUserId(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<ItemDtoWithBookings> items = itemRepository.findAllByOwner(userId).stream()
                .map(ItemMapper::toItemDtoWithBookings)
                .collect(Collectors.toList());
        for (ItemDtoWithBookings i : items) {
            List<Booking> bookings = bookingRepository.findAllByItem_Id(i.getId());
            if (bookings.isEmpty() || i.getOwner() != userId) {
                continue;
            }
            i.setLastBooking(BookingMapper.toBookingDtoItem(getLastBooking(bookings)));
            i.setNextBooking(BookingMapper.toBookingDtoItem(getNextBooking(bookings)));
        }
        return items;
    }

    public Comment addComment(Comment comment, long userId, long itemId) {
        if (comment.getText().isBlank()) {
            throw new ValidationException("Text is empty");
        }
        List<Long> bookings = bookingRepository.findAllByItem_IdAndEndIsBefore(itemId, LocalDateTime.now()).stream()
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
}
