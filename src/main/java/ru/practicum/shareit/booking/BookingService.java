package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {

    Booking addBooking(Booking booking, long userId);

    Booking getBookingById(long bookingId, long userId);

    Booking updateBooking(long userId, long bookingId, boolean approved);

    void deleteBooking(long bookingId, long userId);

    List<Booking> getAllBookingsByBookerIdAndState(long userId, String state);

    List<Booking> getAllBookingsByOwnerIdAndState(long userId, String state);
}
