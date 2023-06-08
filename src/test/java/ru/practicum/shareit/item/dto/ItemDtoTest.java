package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> json;
    @Test
    void testItemDto() throws Exception {
        ItemDto itemDto = new ItemDto();

        itemDto.setName("вещь");
        itemDto.setId(1);
        itemDto.setDescription("описание");
        itemDto.setIsFree(true);
        itemDto.setUser(new User());
        itemDto.setRequestId(1);
        itemDto.setNextBooking(new BookingDto());
        itemDto.setComments(List.of(new CommentDto()));
        itemDto.setLastBooking(new BookingDto());
        itemDto.setNextBooking(new BookingDto());

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("вещь");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("описание");
    }
}