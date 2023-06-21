package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;

import static ru.practicum.shareit.constants.Variables.USER_HEADER;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final RequestClient requestClient;

    @PostMapping()
    public ResponseEntity<Object> createRequest(@RequestHeader(USER_HEADER) Integer userId,
                                                @Valid @RequestBody ItemRequestDto requestDto) {
        return requestClient.createRequest(requestDto, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> getOwnersAllRequests(@RequestHeader(USER_HEADER) Integer userId) {
        return requestClient.getOwnersAllRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(USER_HEADER) Integer userId,
                                                    @RequestParam(defaultValue = "null", required = false) String from,
                                                    @RequestParam(defaultValue = "null", required = false) String size) {
        return requestClient.getAllRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(USER_HEADER) Integer userId,
                                          @PathVariable("requestId") Integer requestId) {
        return requestClient.getRequest(userId, requestId);
    }
}
