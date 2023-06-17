package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentTest {
    @Autowired
    private JacksonTester<Comment> json;

    @Test
    void testComment() throws Exception {
        Comment comment = new Comment();
        comment.setText("вещь");
        comment.setAuthor(new User());
        comment.setId(1);
        comment.setCreated(LocalDateTime.now());

        JsonContent<Comment> result = json.write(comment);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("вещь");
    }

}