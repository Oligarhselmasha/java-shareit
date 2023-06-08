package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemTest {
    @Autowired
    private JacksonTester<Item> json;

    @Test
    void testItem() throws Exception {
        Item item = new Item();
        item.setId(1);
        item.setName("вещь");
        item.setDescription("описание");
        item.setUser(new User());
        item.setIsFree(true);
        item.setRequestId(1);
        item.setComments(List.of(new CommentDto()));
        item.setNextBooking(new BookingDto());
        item.setLastBooking(new BookingDto());

        JsonContent<Item> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("описание");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("вещь");
    }
}