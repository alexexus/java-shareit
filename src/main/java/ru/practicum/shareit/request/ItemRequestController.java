package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.OnCreate;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    public static final String USER_ID = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader(USER_ID) long userId,
                                         @Validated(OnCreate.class) @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestMapper.toItemRequestDto(itemRequestService.addItemRequest(
                itemRequestMapper.toItemRequest(itemRequestDto), userId));
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader(USER_ID) long userId,
                                             @PathVariable long requestId) {
        return itemRequestMapper.toItemRequestDto(itemRequestService.getItemRequestById(requestId, userId));
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequestsPageable(@RequestHeader(USER_ID) long userId,
                                                           @RequestParam(name = "from", required = false) Integer from,
                                                           @RequestParam(name = "size", required = false) Integer size) {
        return itemRequestService.getAllItemRequestsPageable(userId, from, size).stream()
                .map(itemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader(USER_ID) long userId) {
        return itemRequestService.getAllItemRequests(userId).stream()
                .map(itemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }
}
