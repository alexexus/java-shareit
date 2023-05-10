package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {

    Booking addBooking(Booking booking, long userId);

    Booking getBookingById(long bookingId, long userId);

    Booking updateBooking(long userId, long bookingId, boolean approved);

    void deleteBooking(long bookingId, long userId);

    List<Booking> getAllBookingsByBookerIdAndState(long userId, StateConstant state);

    List<Booking> getAllBookingsByBookerId(long userId);

    List<Booking> getAllBookingsByBookerIdAndPast(long userId);

    List<Booking> getAllBookingsByBookerIdAndFuture(long userId);

    List<Booking> getAllBookingsByOwnerId(long userId);

    List<Booking> getAllBookingsByOwnerIdAndState(long userId, StateConstant state);

    List<Booking> getAllBookingsByOwnerIdAndPast(long userId);

    List<Booking> getAllBookingsByOwnerIdAndFuture(long userId);
}
