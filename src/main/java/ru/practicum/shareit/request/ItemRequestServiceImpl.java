package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequest addItemRequest(ItemRequest itemRequest, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequestRepository.save(itemRequest);
    }

    @Override
    public ItemRequest getItemRequestById(long id, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item request not found"));
        itemRequest.setItems(itemRepository.findByRequestId(id));
        return itemRequest;
    }

    @Override
    public List<ItemRequest> getAllItemRequestsPageable(long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (from == null || size == null) {
            return itemRequestRepository.findByRequestorId(userId);
        }
        if (size < 1 || from < 0) {
            throw new ValidationException("Size cannot be less than 1 and from cannot be less than 0");
        }
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(userId,
                PageRequest.of(from / size, size));
        itemRequests.forEach(itemRequest -> itemRequest.setItems(itemRepository.findByRequestId(itemRequest.getId())));
        return itemRequests;
    }

    @Override
    public List<ItemRequest> getAllItemRequests(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorId(userId);
        itemRequests.forEach(itemRequest -> itemRequest.setItems(itemRepository.findByRequestId(itemRequest.getId())));
        return itemRequests;
    }
}
