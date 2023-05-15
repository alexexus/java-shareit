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
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(bookingDto.getBooker())
                .status(bookingDto.getStatus())
                .build();
    }

    public BookingDtoItem toBookingDtoItem(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingDtoItem.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
