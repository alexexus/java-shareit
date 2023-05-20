package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdAndStatusIsOrderByStartDesc(Long bookerId, BookingConstant status);

    List<Booking> findByBookerIdAndStatusIsOrderByStartDesc(Long bookerId, BookingConstant status, Pageable pageable);

    List<Booking> findByBookerIdOrderByStartDesc(long userId);

    List<Booking> findByBookerIdOrderByStartDesc(long userId, Pageable pageable);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerIdAndEndIsAfterOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndEndIsAfterOrderByStartDesc(Long bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long bookerId,
                                                                              LocalDateTime start,
                                                                              LocalDateTime end);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long bookerId,
                                                                              LocalDateTime start,
                                                                              LocalDateTime end,
                                                                              Pageable pageable);

    List<Booking> findByItemOwnerAndStatusIsOrderByStartDesc(Long itemOwner, BookingConstant status);

    List<Booking> findByItemOwnerAndStatusIsOrderByStartDesc(Long itemOwner, BookingConstant status, Pageable pageable);

    List<Booking> findByItemOwnerOrderByStartDesc(long userId);

    List<Booking> findByItemOwnerOrderByStartDesc(long userId, Pageable pageable);

    List<Booking> findByItemOwnerAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findByItemOwnerAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime end, Pageable pageable);

    List<Booking> findByItemOwnerAndEndIsAfterOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findByItemOwnerAndEndIsAfterOrderByStartDesc(long userId, LocalDateTime end, Pageable pageable);

    List<Booking> findByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId,
                                                                               LocalDateTime start,
                                                                               LocalDateTime end);

    List<Booking> findByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId,
                                                                               LocalDateTime start,
                                                                               LocalDateTime end,
                                                                               Pageable pageable);

    List<Booking> findByItemId(long itemId);

    List<Booking> findByItemIdAndEndIsBefore(Long itemId, LocalDateTime end);
}
