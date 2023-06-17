package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RequestTest {
    @Autowired
    private JacksonTester<Request> json;

    @Test
    void testRequest() throws Exception {
        Item item = new Item();
        item.setName("вещь");
        Request request = new Request();
        request.setUser(new User(1, "Кирилл", "kirill-bulanov@yandex.ru"));
        request.setCreated(LocalDateTime.now());
        request.setItems(List.of(item));
        request.setId(1);
        request.setDescription("запрос");

        JsonContent<Request> result = json.write(request);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("запрос");
    }
}