package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.MissingException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemsMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestFrontDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;
    private final ItemsMapper itemsMapper;


    @Override
    public ItemRequestFrontDto createRequest(ItemRequestDto requestDto, Integer userId) {
        User requestingUser = userRepository.findById(userId).orElseThrow(() ->
                new MissingException("is not exist"));
        Request request = requestMapper.toRequest(requestDto);
        request.setCreated(LocalDateTime.now());
        request.setUser(requestingUser);
        requestRepository.save(request);
        return toRequestFrontDto(request);
    }

    @Override
    public List<ItemRequestFrontDto> getOwnersAllRequests(Integer userId) {
        User requestingUser = userRepository.findById(userId).orElseThrow(() ->
                new MissingException("is not exist"));
        List<Request> requests = requestRepository.findByUser_IdOrderByCreatedDesc(userId);
        List<ItemRequestFrontDto> itemRequestFrontDtos = new ArrayList<>();
        for (Request request : requests) {
            itemRequestFrontDtos.add(toRequestFrontDto(request));
        }
        return itemRequestFrontDtos;
    }

    @Override
    public List<ItemRequestFrontDto> getAllRequests(String from, String size, Integer userId) {
        List<Request> requests = requestRepository.findByUser_IdNotOrderByCreatedDesc(userId);
        List<ItemRequestFrontDto> itemRequestFrontDtos = new ArrayList<>();
        for (Request request : requests) {
            itemRequestFrontDtos.add(toRequestFrontDto(request));
        }
        if (from.equals("null") || size.equals("null")) {
            return itemRequestFrontDtos;
        }
        int fromInt = Integer.parseInt(from);
        int sizeInt = Integer.parseInt(size);
        if (fromInt < 0 || sizeInt <= 0) {
            throw new ValidationException("wrong parameters");
        }
        List<ItemRequestFrontDto> requestForReceive;
        if (fromInt + sizeInt >= itemRequestFrontDtos.size()) {
            sizeInt = itemRequestFrontDtos.size();
            requestForReceive = itemRequestFrontDtos.subList(fromInt, sizeInt);
            return requestForReceive;
        }
        requestForReceive = itemRequestFrontDtos.subList(fromInt, fromInt + sizeInt);
        return requestForReceive;
    }

    @Override
    public ItemRequestFrontDto getRequest(Integer userId, Integer requestId) {
        User requestingUser = userRepository.findById(userId).orElseThrow(() ->
                new MissingException("is not exist"));
        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new MissingException("request is not exist"));
        return toRequestFrontDto(request);
    }

    private ItemRequestFrontDto toRequestFrontDto(Request request) {
        ItemRequestFrontDto itemRequestFrontDto = new ItemRequestFrontDto();
        itemRequestFrontDto.setId(request.getId());
        List<ItemDto> itemDtos = new ArrayList<>();
        List<Item> requestsItems = request.getItems();
        if (requestsItems != null) {
            for (Item requestsItem : requestsItems) {
                ItemDto itemDto = itemsMapper.getItemDto(requestsItem);
                itemDto.setRequestId(requestsItem.getRequest().getId());
                itemDtos.add(itemDto);
            }
        }
        itemRequestFrontDto.setDescription(request.getDescription());
        itemRequestFrontDto.setCreated(request.getCreated());
        itemRequestFrontDto.setItems(itemDtos);
        return itemRequestFrontDto;
    }
}
