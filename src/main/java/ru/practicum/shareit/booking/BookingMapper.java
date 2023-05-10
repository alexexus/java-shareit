package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.item.Item;

@UtilityClass
public class BookingMapper {

    public static Booking toBooking(BookingDto bookingDto) {
        Item item = new Item();
        item.setId(bookingDto.getItemId());
        return new Booking(bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                bookingDto.getBooker(),
                bookingDto.getStatus());
    }

    public static BookingDtoItem toBookingDtoItem(Booking booking) {
        if (booking == null) {
            return null;
        }
        return new BookingDtoItem(booking.getId(),
                booking.getBooker().getId());
    }
}
