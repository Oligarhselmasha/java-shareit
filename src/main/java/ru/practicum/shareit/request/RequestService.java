package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestFrontDto;

import java.util.List;

public interface RequestService {
    ItemRequestFrontDto createRequest(ItemRequestDto requestDto, Integer userId);

    List<ItemRequestFrontDto> getOwnersAllRequests(Integer userId);

    List<ItemRequestFrontDto> getAllRequests(String from, String size, Integer userId);

    ItemRequestFrontDto getRequest(Integer userId, Integer requestId);
}
