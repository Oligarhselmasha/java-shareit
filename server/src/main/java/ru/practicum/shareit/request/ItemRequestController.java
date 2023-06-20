package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestFrontDto;

import java.util.List;

import static ru.practicum.shareit.constants.Variables.USER_HEADER;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final RequestService requestService;

    @PostMapping()
    public ItemRequestFrontDto createRequest(@RequestHeader(USER_HEADER) Integer userId,
                                             @RequestBody ItemRequestDto requestDto) {
        return requestService.createRequest(requestDto, userId);
    }

    @GetMapping()
    public List<ItemRequestFrontDto> getOwnersAllRequests(@RequestHeader(USER_HEADER) Integer userId) {
        return requestService.getOwnersAllRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestFrontDto> getAllRequests(@RequestHeader(USER_HEADER) Integer userId,
                                                    @RequestParam(defaultValue = "null", required = false) String from,
                                                    @RequestParam(defaultValue = "null", required = false) String size) {
        return requestService.getAllRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestFrontDto getRequest(@RequestHeader(USER_HEADER) Integer userId,
                                          @PathVariable("requestId") Integer requestId) {
        return requestService.getRequest(userId, requestId);
    }
}
