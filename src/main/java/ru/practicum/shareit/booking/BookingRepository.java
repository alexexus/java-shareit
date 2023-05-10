package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking as b where b.booker.id = ?1 and b.status = ?2 order by b.start desc")
    List<Booking> findAllByBookerIdAndState(long userId, BookingConstant state);

    List<Booking> findAllByBooker_IdOrderByStartDesc(long userId);

    List<Booking> findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findAllByBooker_IdAndEndIsAfterOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long bookerId, LocalDateTime start, LocalDateTime end);

    @Query("select b from Booking as b where b.item.owner = ?1 and b.status = ?2 order by b.start desc")
    List<Booking> findAllByItemOwnerAndState(long userId, BookingConstant state);

    List<Booking> findAllByItem_OwnerOrderByStartDesc(long userId);

    List<Booking> findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findAllByItem_OwnerAndEndIsAfterOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItem_Id(long itemId);

    List<Booking> findAllByItem_IdAndEndIsBefore(Long itemId, LocalDateTime end);
}
