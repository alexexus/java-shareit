package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i " +
            "where (upper(i.name) like upper(concat('%', :name, '%')) " +
            "or upper(i.description) like upper(concat('%', :description, '%'))) " +
            "and i.available = true")
    List<Item> searchByText(String name, String description);

    @Query("select i from Item i " +
            "where (upper(i.name) like upper(concat('%', :name, '%')) " +
            "or upper(i.description) like upper(concat('%', :description, '%'))) " +
            "and i.available = true")
    List<Item> searchByText(String name, String description, Pageable pageable);

    List<Item> findByOwnerOrderById(long userId);

    List<Item> findByOwnerOrderById(long userId, Pageable pageable);

    List<Item> findByRequestId(long requestId);
}
