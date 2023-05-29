package ru.practicum.shareit.request;

import java.util.List;

public interface ItemRequestService {

    ItemRequest addItemRequest(ItemRequest itemRequest, long userId);

    ItemRequest getItemRequestById(long id, long userId);

    List<ItemRequest> getAllItemRequestsPageable(long userId, Integer from, Integer size);

    List<ItemRequest> getAllItemRequests(long userId);
}
