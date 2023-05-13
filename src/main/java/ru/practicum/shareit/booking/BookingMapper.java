package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.item.Item;

@Component
public class BookingMapper {

    public Booking toBooking(BookingDto bookingDto) {
        Item item = new Item();
        item.setId(bookingDto.getItemId());
        return new Booking(bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                bookingDto.getBooker(),
                bookingDto.getStatus());
    }

    public BookingDtoItem toBookingDtoItem(Booking booking) {
        if (booking == null) {
            return null;
        }
        return new BookingDtoItem(booking.getId(),
                booking.getBooker().getId());
    }
}
