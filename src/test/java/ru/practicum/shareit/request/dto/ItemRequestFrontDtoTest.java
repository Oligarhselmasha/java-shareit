package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestFrontDtoTest {
    @Autowired
    private JacksonTester<ItemRequestFrontDto> json;

    @Test
    void testItemRequestFrontDto() throws Exception {
        ItemRequestFrontDto itemRequestFrontDto = new ItemRequestFrontDto();
        ItemDto item = new ItemDto();
        item.setName("вещь");

        itemRequestFrontDto.setItems(List.of(item));
        itemRequestFrontDto.setCreated(LocalDateTime.now());
        itemRequestFrontDto.setId(1);
        itemRequestFrontDto.setDescription("Описание");


        JsonContent<ItemRequestFrontDto> result = json.write(itemRequestFrontDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Описание");
    }

}