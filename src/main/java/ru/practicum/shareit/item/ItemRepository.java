package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByNameContainingIgnoreCaseAndAvailableIsTrueOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
            String name, String description);

    List<Item> findByNameContainingIgnoreCaseAndAvailableIsTrueOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
            String name, String description, Pageable pageable);

    List<Item> findByOwner(long userId);

    List<Item> findByOwner(long userId, Pageable pageable);

    List<Item> findByRequestId(long requestId);
}
