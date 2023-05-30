package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.MissingException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemsMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
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
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemsMapper itemMapper;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;

    private final RequestMapper requestMapper;

    @Override
    public ItemDto createItem(ItemDto itemDto, Integer userId) {
        Item item = itemMapper.toItem(itemDto);
        item.setUser(userRepository.findById(userId).orElseThrow(() ->
                new MissingException("is not exist")));
        if (itemDto.getRequestId() != null) {
            item.setRequest(requestRepository.findById(itemDto.getRequestId()).orElse(null));
            item.setRequestId(itemDto.getRequestId());
        }
        item = itemRepository.save(item);
        if (item.getRequestId() != null) {
            List<Item> items = new ArrayList<>(List.of(item));
            Request request = requestRepository.findById(itemDto.getRequestId()).orElseThrow();
            request.setItems(items);
            requestRepository.save(request);
        }
        return toItemDto(item);
    }

    @Override
    public ItemDto updateItem(@NotNull ItemDto itemDto, Integer userId, Integer itemId) {
        Item itemBeforeUpdate = itemRepository.findById(itemId).orElseThrow(() ->
                new MissingException("is not exist"));
        if (itemDto.getId() == null) {
            itemDto.setId(itemId);
        }
        if (itemDto.getName() == null) {
            itemDto.setName(itemBeforeUpdate.getName());
        }
        if (itemDto.getDescription() == null) {
            itemDto.setDescription(itemBeforeUpdate.getDescription());
        }
        if (itemDto.getIsFree() == null) {
            itemDto.setIsFree(itemBeforeUpdate.getIsFree());
        }
        Item item = itemMapper.toItem(itemDto);
        item.setUser(userRepository.findById(userId).orElseThrow(() ->
                new MissingException("is not exist")));
        if (itemRepository.findById(itemId).orElseThrow().getUser().getId() != userId) {
            throw new MissingException("Isn't user's thing");
        }
        return toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItem(Integer itemId, Integer userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new MissingException("is not exist"));
        List<Booking> bookingsLast = bookingRepository.findByItem_IdAndStartTimeBeforeAndItem_User_IdOrderByEndTimeDesc(itemId,
                LocalDateTime.now(), userId);
        if (!bookingsLast.isEmpty()) {
            Booking lastBooking = bookingsLast.stream().findFirst().orElseThrow();
            BookingDto lastBookingDto = new BookingDto();
            lastBookingDto.setBookingId(lastBooking.getBookingId());
            lastBookingDto.setBookerId(lastBooking.getUser().getId());
            item.setLastBooking(lastBookingDto);
        }
        List<Booking> bookingsNext = bookingRepository.findByItem_IdAndStartTimeAfterAndItem_User_IdIdAndStatusOrderByStartTimeAsc(itemId, LocalDateTime.now(),
                userId, BookingStatus.APPROVED);
        if (!bookingsNext.isEmpty()) {
            Booking nextBooking = bookingsNext.stream().findFirst().orElseThrow();
            BookingDto nextBookingDto = new BookingDto();
            nextBookingDto.setBookingId(nextBooking.getBookingId());
            nextBookingDto.setBookerId(nextBooking.getUser().getId());
            item.setNextBooking(nextBookingDto);
        }
        List<Comment> comments = commentRepository.findByItem_Id(itemId);
        List<CommentDto> commentDtos = new ArrayList<>();
        comments.forEach(comment -> commentDtos.add(commentMapper.toCommentDto(comment)));
        commentDtos.forEach(c -> c.setAuthorName(commentRepository.findById(c.getId()).orElseThrow().getAuthor().getName()));
        item.setComments(commentDtos);
        return toItemDto(item);
    }

    @Override
    public List<ItemDto> getUsersItems(Integer userId) {
        List<Item> items = itemRepository.findByUser_Id(userId);
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            ItemDto itemDto = toItemDto(item);
            itemDto = getItem(itemDto.getId(), userId);
            itemDtos.add(itemDto);
        }
        return itemDtos;
    }

    @Override
    public List<ItemDto> getItemByQuery(Long userId, String text) {
        if (text.isBlank() || text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> items = itemRepository.findByDescriptionContainsIgnoreCaseAndIsFreeTrue(text);
        List<ItemDto> itemDtos = new ArrayList<>();
        items.forEach(i -> itemDtos.add(toItemDto(i)));
        return itemDtos;
    }

    @Override
    public CommentDto createItemsComment(CommentDto commentDto, Integer userId, Integer itemId) {
        if (commentDto.getText().isBlank() || commentDto.getText().isEmpty()) {
            throw new ValidationException("text is empty");
        }
        if (bookingRepository.existsByItem_IdAndUser_IdAndStatusAndEndTimeBefore(itemId, userId, BookingStatus.APPROVED,
                LocalDateTime.now())) {
            Comment comment = new Comment();
            comment.setItem(itemRepository.findById(itemId).orElseThrow());
            User author = userRepository.findById(userId).orElseThrow();
            comment.setAuthor(author);
            comment.setText(commentDto.getText());
            comment.setCreated(LocalDateTime.now());
            commentRepository.save(comment);
            commentDto = commentMapper.toCommentDto(comment);
            commentDto.setAuthorName(author.getName());
        } else {
            throw new ValidationException("booking ist exist");
        }
        return commentDto;
    }

    @Override
    public void removeItem(Integer userId, Integer itemId) {
        ItemDto item = getItem(itemId, userId);
        if (item.getUser().getId() != userId) {
            throw new ValidationException("Isn't user's thing");
        }
        itemRepository.deleteById(itemId);
    }

    private ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        if (item.getIsFree() != null) {
            itemDto.setIsFree(item.getIsFree());
        }
        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getId());
        }
        if (item.getName() != null) {
            itemDto.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemDto.setDescription(item.getDescription());
        }
        if (item.getUser() != null) {
            itemDto.setUser(item.getUser());
        }
        if (item.getId() != null) {
            itemDto.setId(item.getId());
        }
        if (item.getComments() != null) {
            itemDto.setComments(item.getComments());
        }
        if (item.getLastBooking() != null) {
            itemDto.setLastBooking(item.getLastBooking());
        }
        if (item.getNextBooking() != null) {
            itemDto.setNextBooking(item.getNextBooking());
        }
        return itemDto;
    }
}
